package at.fhooe.mc.server.Services.Optimizer;

import ChargingEnviroment.EvSimChargingPoint;
import at.fhooe.mc.server.Data.*;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Logging.ActionLogger;
import at.fhooe.mc.server.Repository.*;
import at.fhooe.mc.server.Services.WeatherService;
import at.fhooe.mc.server.Simulation.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class Optimizer implements Runnable, UpdateOptimizer {
    //ModbusConnector modbusConnector = new ModbusConnector();
    ArrayList<Session> sessions = new ArrayList<>();
    ArrayList<HourlyWeatherForecast> weatherForecasts = new ArrayList<>();
    Double availableSolarPower = 0.0;
    Simulation simulation = new Simulation();
    SolarPanels solarPanels;

    @Autowired
    SolarPanelsRepository solarPanelsRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    WeatherForecastRepository weatherForecastRepository;
    @Autowired
    WeatherRepository weatherRepository;
    @Autowired
    SystemReportRepository systemReportRepository;
    @Autowired
    WeatherService weatherService;

    @Override
    public void run() {
        ActionLogger.writeLineToFile("Optimizer Started!");
    }

    @PostConstruct
    public void initialize() {
        this.getLastWeatherDataInDatabase();
        this.updateAviableSolarPower();
        this.getSessionsInDatabase();
    }

    @Override
    public void updateWeatherForecast(ArrayList<HourlyWeatherForecast> weatherForecasts) {
        checkWeatherForecast(weatherForecasts);
        ActionLogger.writeLineToFile("New Weather Forecast was loaded!");
        optimizeAllSessions();
    }

    @Override
    public void addSession(Session session) {
        this.sessions.add(session);
        session.setStartDate(new Date());
        simulation.setVehicleToChargingPoint(session);
        simulation.getChargingPoint(session.getLoadingPort().getPort()).startCharging();
        optimizeAllSessions();
    }

    @Override
    public void stopSession(Session session, boolean reOptimize) {
        //this.sessions.remove(session);
        //sessionRepository.delete(session);
        simulation.getChargingPoint(session.getLoadingPort().getPort()).stopCharging();
        simulation.getChargingPoint(session.getLoadingPort().getPort()).removeVehicleFromPoint();

        if(reOptimize){
            optimizeAllSessions();
        }
    }

    @Override
    public void pauseSession(Session session) {
        pauseChargingProcessByUser(session);
        optimizeAllSessions();
    }

    @Override
    public void restartSession(Session session) {
        session.setTemporaryPausedByUser(false);
        optimizeAllSessions();
    }

    @Override
    public void updateAviableSolarPower() {
        this.availableSolarPower = simulation.getSolar().hourOutput();
    }

    /**
     * Starts the recursive function to divide the energie between the sessions.
     */
    private void divideAvailableSolarPower() {
        sortSessionsAfterRating();

        coverBasicSupply();
        if (!sessions.isEmpty()) {
            adjustSessions(0, availableSolarPower);
        }
    }

    private void optimizeAllSessions() {
        for (Session session : sessions) {
            calculateLeftOverCapacity(session);
            calculateLeftOverTime(session);
            calculateMinimumPower(session);
        }
        pauseSessionsAccordingToWeatherAndSolar();
        divideAvailableSolarPower();
        createSystemReport();
    }

    private void createSystemReport(){
        SystemReport report = new SystemReport(sessions, availableSolarPower);
        systemReportRepository.save(report);
    }

    /**
     * The aviable solar power will supply every session with the minimum power supply. When solar energy is not aviable any more the session will recive theire minimum power
     */
    private void coverBasicSupply() {
        for (Session session : sessions) {
            if (!session.isTemporaryPausedBySystem() && !session.isTemporaryPausedByUser()) {
                if (availableSolarPower != 0.0) {
                    if (session.getMinPower() > availableSolarPower) {
                        setOptimizedChargingSettingsOnSession(session, session.getMinPower(), availableSolarPower);
                        availableSolarPower = 0.0;
                        return;
                    } else {
                        setOptimizedChargingSettingsOnSession(session, session.getMinPower(), session.getMinPower());
                        availableSolarPower -= session.getMinPower();
                    }
                } else {
                    setNonOptimizedChargingSettingsOnSession(session, session.getMinPower());
                }
            }
        }
    }

    private void setOptimizedChargingSettingsOnSession(Session session, double chargingPower, double optimizedPower) {
        session.setChargingPower(chargingPower);
        session.setOptimizedPower(optimizedPower);
        session.setOptimized(true);
        sessionRepository.save(session);
    }

    private void setNonOptimizedChargingSettingsOnSession(Session session, double chargingPower) {
        session.setChargingPower(chargingPower);
        session.setOptimized(false);
        sessionRepository.save(session);
    }

    /**
     * Here the rest of the aviable solar power will get devided to the sessions and then adjust those sessions.
     *
     * @param pos
     * @param availableSolarPower
     */
    private void adjustSessions(int pos, double availableSolarPower) {

        if (sessions.size() <= pos) {
            if (availableSolarPower != 0.0) {
                adjustFallBackSessions(availableSolarPower);
            }
            return;
        }

        Session session = sessions.get(pos);
        double solarPowerForTheNextSession = 0.0;

        if (!session.isTemporaryPausedBySystem() && !session.isTemporaryPausedByUser()) {
            if (!session.isFallBack()) {
                if (!session.isSlowMode() && !session.getCar().isOnePhase()) { //Session is normal and has 3 phase
                    Map<Integer, Double> map = splitAvailableSolarPower(22000.0, availableSolarPower);

                    if(map.get(0) == 22000.0){
                        setOptimizedChargingSettingsOnSession(session, 22000.0, 22000.0);
                    } else {
                        setOptimizedChargingSettingsOnSession(session, session.getChargingPower() + map.get(0), session.getOptimizedPower() + map.get(0));
                    }
                    solarPowerForTheNextSession = map.get(1);
                } else if (session.isSlowMode() && !session.getCar().isOnePhase()) { //Session is slow mode and has 3 phase
                    Map<Integer, Double> map = splitAvailableSolarPower(11000.0, availableSolarPower);
                    if(map.get(0) == 11000.0){
                        setOptimizedChargingSettingsOnSession(session, 11000.0, 11000.0);
                    } else {
                        setOptimizedChargingSettingsOnSession(session, session.getChargingPower() + map.get(0), session.getOptimizedPower() + map.get(0));
                    }
                    solarPowerForTheNextSession = map.get(1);
                } else if (session.getCar().isOnePhase()) { //Session is one phase
                    Map<Integer, Double> map = splitAvailableSolarPower(3700.0, availableSolarPower);
                    if(map.get(0) == 3700.0){
                        setOptimizedChargingSettingsOnSession(session, 3700.0, 3700.0);
                    } else {
                        setOptimizedChargingSettingsOnSession(session, session.getChargingPower() + map.get(0), session.getOptimizedPower() + map.get(0));
                    }
                    solarPowerForTheNextSession = map.get(1);
                }
            }

            applyToChargingPoint(session, session.getChargingPower());
            sessionRepository.save(session);
            adjustSessions(pos += 1, solarPowerForTheNextSession);
        }
    }

    private Map<Integer, Double> splitAvailableSolarPower(double maximumPower, double availableSolarPower) {
        double forSession = availableSolarPower * 0.65;
        double leftOverForOthers = 0.0;

        if (forSession < 3700.0) {
            forSession = availableSolarPower;
        } else if (forSession > maximumPower) {
            forSession = maximumPower;
            leftOverForOthers = availableSolarPower - forSession;
        } else {
            leftOverForOthers = availableSolarPower - forSession;
        }

        Map<Integer, Double> map = new HashMap<>();
        map.put(0, forSession);
        map.put(1, leftOverForOthers);
        return map;
    }

    private void adjustFallBackSessions(double leftOverSolarPower) {
        double solarPowerForEachSession = leftOverSolarPower / countFallBackSessions();
        if (solarPowerForEachSession > 3700) {
            for (Session session : sessions) {
                if (session.isFallBack()) {
                    session.setOptimized(true);
                    session.setChargingPower(solarPowerForEachSession);
                    session.setOptimizedPower(solarPowerForEachSession);
                    applyToChargingPoint(session, solarPowerForEachSession);
                }
            }
        }
    }

    private int countFallBackSessions() {
        int count = 0;
        for (Session session : sessions) {
            if (session.isFallBack()) {
                count += 1;
            }
        }
        return count;
    }

    private void applyToChargingPoint(Session session, double power) {
        EvSimChargingPoint simulationPoint = simulation.getChargingPoint(session.getLoadingPort().getPort());
        simulationPoint.changeChargingSpeedOnPoint(power);
    }

    private void sortSessionsAfterRating() {
        sessions.sort((Session p1, Session p2) -> p1.getMinPower().compareTo(p2.getMinPower()));
    }

    private void calculateMinimumPower(Session session) {
        if(session.getCurrentCapacity() >= session.getEndCapacity()){
            notifyUser("Vehicle is already optimal charged!");
            stopSession(session, false);
            return;
        }

        Double minPower = session.getRestCapacity() / session.getTimeToEnd();
        session.setMinPower(minPower);
        checkMinimumPower(session);
    }

    private void checkMinimumPower(Session session) {
        if (session.getMinPower() < 0) {
            notifyUser("Vehicle is already optimal charged!");
            stopSession(session, false);
            return;
        }

        if (session.getCar().isOnePhase()) {
            if (session.getMinPower() > 3700) {
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
            sessionRepository.save(session);
            return;
        }

        if (!session.isSlowMode()) {
            if (session.getMinPower() > 22000) {
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
        } else {
            if (session.getMinPower() > 11000) {
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
        }

        sessionRepository.save(session);
    }

    private void notifyUser(String msg) {
        System.out.println(msg);
    }

    private void moveSessionToFallBack(Session session) {
        session.setFallBack(true);
        session.setMinPower(3700.0);
        setNonOptimizedChargingSettingsOnSession(session, session.getMinPower());
    }

    private double getCurrentCapacityForSession(Session session) {
        return simulation.getChargingPoint(session.getLoadingPort().getPort()).getEvSimVehicle().getEvSimBattery().getCurrentCapacity();
    }

    private double calculateLeftOverTime(Session session) {
        long tmp = session.getEndDate().getTime() - new Date().getTime();
        Date dateWithPuffer = new Date(tmp);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithPuffer);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String dateString = hours + "." + minutes + seconds;
        double diff = Double.parseDouble(dateString);
        session.setTimeToEnd(diff);

        return diff;
    }

    private double calculateLeftOverCapacity(Session session) {
        Double currentCapacity = getCurrentCapacityForSession(session);
        Double diff = session.getEndCapacity() - currentCapacity;
        session.setRestCapacity(diff);
        return diff;
    }


    private void checkWeatherForecast(ArrayList<HourlyWeatherForecast> weatherForecasts) {
        if (this.weatherForecasts.isEmpty()) {
            this.weatherForecasts = weatherForecasts;
        } else {
            deleteOldWeatherData();
            this.weatherForecasts = weatherForecasts;
        }

        fillTimeBetweenHours();
        estimateForecastSolarPower();
    }

    /**
     * Fills the time between the downloaded hours. The Api changed their plan so only for every 3 hours data is available.
     */
    protected void fillTimeBetweenHours() {
        if(this.weatherForecasts.isEmpty()){return;}
        ArrayList<HourlyWeatherForecast> tmpWeatherForecasts = new ArrayList<>();
        for (HourlyWeatherForecast weatherForecast : this.weatherForecasts) {
            for (int i = 1; i <= 2; i++) {
                HourlyWeatherForecast tmpWeatherForecast = weatherForecast.copyWeatherForecast();
                Date time = tmpWeatherForecast.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(time);
                calendar.add(Calendar.HOUR_OF_DAY, i);
                tmpWeatherForecast.setTime(calendar.getTime());
                weatherForecastRepository.save(tmpWeatherForecast);
                tmpWeatherForecasts.add(tmpWeatherForecast);
            }
        }

        long currentDateTime = new Date().getTime();
        long firstWeatherForecastTime = this.weatherForecasts.get(0).getTime().getTime();
        long diff = firstWeatherForecastTime - currentDateTime;
        int diffHours = (int) diff / (60 * 60 * 1000) % 24;

        for (int i = 1; i <= diffHours + 1; i++) {
            HourlyWeatherForecast tmpWeatherForecast = this.weatherForecasts.get(0).copyWeatherForecast();
            Date time = tmpWeatherForecast.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.HOUR_OF_DAY, -i);
            tmpWeatherForecast.setTime(calendar.getTime());
            weatherForecastRepository.save(tmpWeatherForecast);
            tmpWeatherForecasts.add(tmpWeatherForecast);
        }

        weatherForecasts.addAll(tmpWeatherForecasts);
        weatherForecasts.sort((HourlyWeatherForecast p1, HourlyWeatherForecast p2) -> p1.getTime().compareTo(p2.getTime()));
    }

    protected void deleteOldWeatherData() {
        for (HourlyWeatherForecast weatherForecast : this.weatherForecasts) {
            weatherForecastRepository.delete(weatherForecast);
        }
    }

    private void estimateForecastSolarPower() {
        SolarPanels solarPanels = solarPanelsRepository.findSolarPanelsById(1);
        for (HourlyWeatherForecast weatherForecast : weatherForecasts) {
            double possiblePowerPlain = solarPanels.getPossiblePowerForHour(weatherForecast.getTemp());
            double onePercent = possiblePowerPlain / 100;
            double possiblePower = onePercent * (100 - weatherForecast.getClouds());
            if (weatherForecast.isDuringDayLight()) {
                weatherForecast.setPossiblePower(possiblePower);
            } else {
                weatherForecast.setPossiblePower(0.0);
            }
            weatherForecastRepository.save(weatherForecast);
        }
    }


    /**
     * Check if the current aviable solar power is enough for all sessions then
     */
    private void pauseSessionsAccordingToWeatherAndSolar() {
        double neededSolarPower = getNeededSolarPower(sessions);
        if(availableSolarPower < neededSolarPower){
            ArrayList<Session> possibleSessionsToPause = getPossibleSessionsToPause();
            possibleSessionsToPause.sort((Session p1, Session p2) -> p1.getMinPower().compareTo(p2.getMinPower()));
            checkNeededPowerAndSolarRange(possibleSessionsToPause);
        }
    }

    private void checkNeededPowerAndSolarRange(ArrayList<Session> sessions){
        double neededSolarPower = getNeededSolarPower(sessions);
        double aviableSolarPower = getPossibleSolarPowerInTheNextThreeHour() * 0.7;

        if(sessions.isEmpty()){
            return;
        }

        if(neededSolarPower > aviableSolarPower){
            sessions.remove(0);
            checkNeededPowerAndSolarRange(sessions);
        } else {
            for(Session session : sessions){
                pauseChargingProcessBySystem(session);
            }
        }
    }

    private double getPossibleSolarPowerInTheNextThreeHour(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 3);
        double possiblePower = 0.0;

        for(HourlyWeatherForecast weatherForecast : this.weatherForecasts){
            if(weatherForecast.getTime().getTime() <= cal.getTime().getTime()){
                possiblePower += weatherForecast.getPossiblePower();
            }
        }

        return possiblePower;
    }

    private ArrayList<Session> getPossibleSessionsToPause(){
        ArrayList<Session> sessions = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 3);

        for(Session session : this.sessions){
            if(session.getEndDate().getTime() > cal.getTime().getTime()){
                if(session.getMinPower() < 8000){
                    sessions.add(session);
                }
            }
        }

        return sessions;
    }

    private double getNeededSolarPower(ArrayList<Session> sessions){
        double neededSolarPower = 0.0;
        for(Session session: sessions){
            neededSolarPower += session.getMinPower();
        }

        return  neededSolarPower;
    }

    @Scheduled(initialDelay = 120000, fixedRate = 300000)
    private void monitorSessions() {
        for (Session session : sessions) {

        }
    }

    private void pauseChargingProcessBySystem(Session session) {
        session.setTemporaryPausedBySystem(true);
        simulation.getChargingPoint(session.getLoadingPort().getPort()).stopCharging();
        sessionRepository.save(session);
    }

    private void pauseChargingProcessByUser(Session session) {
        session.setTemporaryPausedByUser(true);
        simulation.getChargingPoint(session.getLoadingPort().getPort()).stopCharging();
        notifyUser("Charging Process successfully paused. ");
        sessionRepository.save(session);
    }

    /**
     * Loads current running sessions and removes old ones.
     */
    private void getSessionsInDatabase() {
        Date date = new Date();
        this.sessions.addAll(sessionRepository.findAllSessionsCurrentRunning(date));
        ArrayList<Session> sessionsToDelete = new ArrayList(sessionRepository.findAllSessionNotRunning(date));
        sessionRepository.deleteAll(sessionRepository.findAllSessionNotRunning(date));

        for (Session session : sessions) {
            simulation.setVehicleToChargingPoint(session);
            simulation.getChargingPoint(session.getLoadingPort().getPort()).startCharging();
        }

        optimizeAllSessions();
    }

    private void getLastWeatherDataInDatabase() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 6);
        this.weatherForecasts = new ArrayList(weatherForecastRepository.findNextWeatherForecasts(new Date(), c.getTime()));

        if (this.weatherForecasts.isEmpty()) {
            this.weatherService.gatherWeatherDataForecastAndCurrentSolarData();
        }
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public ArrayList<HourlyWeatherForecast> getWeatherForecasts() {
        return weatherForecasts;
    }

    public void setWeatherForecasts(ArrayList<HourlyWeatherForecast> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    public void setAvailableSolarPower(Double availableSolarPower) {
        this.availableSolarPower = availableSolarPower;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public SolarPanels getSolarPanels() {
        return solarPanels;
    }

    public void setSolarPanels(SolarPanels solarPanels) {
        this.solarPanels = solarPanels;
    }

    public Double getAvailableSolarPower() {
        return availableSolarPower;
    }
}
