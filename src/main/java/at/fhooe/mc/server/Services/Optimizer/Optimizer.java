package at.fhooe.mc.server.Services.Optimizer;

import ChargingEnviroment.EvSimChargingPoint;
import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.SessionChanges;
import at.fhooe.mc.server.Data.SolarPanels;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Logging.ActionLogger;
import at.fhooe.mc.server.Repository.SessionRepository;
import at.fhooe.mc.server.Repository.SolarPanelsRepository;
import at.fhooe.mc.server.Repository.WeatherForecastRepository;
import at.fhooe.mc.server.Repository.WeatherRepository;
import at.fhooe.mc.server.Simulation.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Optimizer implements Runnable, UpdateOptimizer {
    //ModbusConnector modbusConnector = new ModbusConnector();
    ArrayList<Session> sessions = new ArrayList<>();
    ArrayList<HourlyWeatherForecast> weatherForecasts = new ArrayList<>();
    Double availableSolarPower = 0.0;
    Simulation simulation = new Simulation();
    SolarPanels solarPanels;
    enum WeatherOptimizationModes{
        Normal, Morning, GoodForecast
    }
    WeatherOptimizationModes weatherOptimizationMode = WeatherOptimizationModes.Normal;

    @Autowired
    SolarPanelsRepository solarPanelsRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    WeatherForecastRepository weatherForecastRepository;
    @Autowired
    WeatherRepository weatherRepository;

    @Override
    public void run() {
        ActionLogger.writeLineToFile("Optimizer Started!");
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
        simulation.getChargingPoint(session.getLoadingport().getPort()).startCharging();
        //createSessionChange(session);
        optimizeAllSessions();
    }

    @Override
    public void stopSession(Session session) {
        this.sessions.remove(session);
        sessionRepository.delete(session);
        simulation.getChargingPoint(session.getLoadingport().getPort()).stopCharging();
        simulation.getChargingPoint(session.getLoadingport().getPort()).removeVehicleFromPoint();
        //ActionLogger.writeLineToFile("Session was stopped: " + session.toString());
        optimizeAllSessions();
    }

    @Override
    public void pauseSession(Session session) {
        pauseChargingProcessByUser(session);
       // ActionLogger.writeLineToFile("Session was paused: " + session.toString());
        optimizeAllSessions();
    }

    @Override
    public void restartSession(Session session) {
        session.setTemporaryPausedByUser(false);
        //ActionLogger.writeLineToFile("Session was restarted: " + session.toString());
        optimizeAllSessions();
    }

    /**
     * Starts the recursive function to divide the energie between the sessions.
     */
    private void divideAvailableSolarPower() {
        sortSessionsAfterRating();

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
        setStrategieAccordingToWeatherAndSolar();
        divideAvailableSolarPower();
    }

    private void adjustSessions(int pos, double availableSolarPower) {

        if (sessions.size() <= pos) {
            if (availableSolarPower != 0.0) {
                adjustFallBackSessions(availableSolarPower);
            }
            return;
        }

        Session session = sessions.get(pos);

        if (!session.isTemporaryPausedBySystem() && !session.isTemporaryPausedByUser()) {
            if (!session.isFallBack()) {
                if (availableSolarPower < 3700) {
                    session.setOptimized(false);
                    applyToChargingPoint(session, session.getMinPower());
                    //ActionLogger.writeLineToFile("Session was NOT optimized: " + session.toString());
                } else {
                    if (session.getMinPower() > 21000 && session.getMinPower() <= 22000) {
                        session.setOptimized(true);
                        availableSolarPower -= session.getMinPower();
                        session.setOptimizedPower(session.getMinPower());
                        adjustSessions(pos += 1, availableSolarPower);
                        //ActionLogger.writeLineToFile("Session was optimized: " + session.toString());
                    } else if (session.getMinPower() < 3700 && session.isOptimized()) {
                        session.setOptimized(false);
                        pauseChargingProcessBySystem(session);
                        adjustSessions(pos += 1, availableSolarPower);
                        //ActionLogger.writeLineToFile("Session was NOT optimized: " + session.toString());
                    } else {
                        if(session.getMinPower() > availableSolarPower){
                            availableSolarPower = 0;
                            session.setOptimized(true);
                            session.setOptimizedPower(session.getMinPower());
                            applyToChargingPoint(session, session.getOptimizedPower());
                            adjustSessions(pos += 1, availableSolarPower);
                            //ActionLogger.writeLineToFile("Session was optimized: " + session.toString());
                        } else {
                            Map<Integer, Double> map = splitAvailableSolarPower(availableSolarPower);
                            session.setOptimized(true);
                            session.setOptimizedPower(map.get(0));
                            applyToChargingPoint(session, session.getOptimizedPower());
                            adjustSessions(pos += 1, map.get(1));
                            //ActionLogger.writeLineToFile("Session was optimized: " + session.toString());
                        }
                    }
                }
            } else {
                applyToChargingPoint(session, 3700);
            }
            sessionRepository.save(session);
        }
    }

    private Map<Integer, Double> splitAvailableSolarPower(double availableSolarPower) {
        double forSession = availableSolarPower * 0.65;
        double leftOverForOthers = 0.0;

        if (forSession < 3700.0) {
            forSession = availableSolarPower;
        } else if (forSession > 22000.0) {
            forSession = 22000.0;
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
        EvSimChargingPoint simulationPoint = simulation.getChargingPoint(session.getLoadingport().getPort());
        simulationPoint.changeChargingSpeedOnPoint(power);
    }

    private void sortSessionsAfterRating() {
        sessions.sort((Session p1, Session p2) -> p1.getMinPower().compareTo(p2.getMinPower()));
    }

    private void calculateMinimumPower(Session session) {
        Double minPower = session.getRestCapacity() / session.getTimeToEnd();
        session.setMinPower(minPower);
        checkMinimumPower(session);
    }

    private void checkMinimumPower(Session session) {
        if (session.getMinPower() < 0) {
            notifyUser("Vehicle is already optimal charged!");
            //ActionLogger.writeLineToFile("Session is done: " + session.toString());
            this.sessions.remove(session);
            sessionRepository.delete(session);
        }

        if (session.getCar().isOnePhase()) {
            if (session.getMinPower() > 3700) {
                notifyUser("Optimaztion not possible in given Time!");
                //ActionLogger.writeLineToFile("Session is not able to full fill requirements and moved to fallback: " + session.toString());
                moveSessionToFallBack(session);
            }
            return;
        }

        if (!session.isSlowMode()) {
            if (session.getMinPower() > 22000) {
                notifyUser("Optimaztion not possible in given Time!");
                //ActionLogger.writeLineToFile("Session is not able to full fill requirements and moved to fallback: " + session.toString());
                moveSessionToFallBack(session);
            }
        } else {
            if (session.getMinPower() > 11000) {
                notifyUser("Optimaztion not possible in given Time!");
                //ActionLogger.writeLineToFile("Session is not able to full fill requirements and moved to fallback: " + session.toString());
                moveSessionToFallBack(session);
            }
        }
    }

    private void notifyUser(String msg) {
        System.out.println(msg);
    }

    private void moveSessionToFallBack(Session session) {
        session.setMinPower(3700.0);
        session.setFallBack(true);
    }

    private double getCurrentCapacityForSession(Session session) {
        return simulation.getChargingPoint(session.getLoadingport().getPort()).getEvSimVehicle().getEvSimBattery().getCurrentCapacity();
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
            return;
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
            double possiblePower = solarPanels.getPossiblePowerForHour(weatherForecast.getTemp());
            if (weatherForecast.isDuringDayLight()) {
                weatherForecast.setPossiblePower(possiblePower);
            } else {
                weatherForecast.setPossiblePower(0.0);
            }
            weatherForecastRepository.save(weatherForecast);
        }
    }
    

    private void setStrategieAccordingToWeatherAndSolar() {
        /**
         * TODO: Define Weather Modes and Pause Sessions, then fire a timer till when the weather could change and restart those sessions. What will happen with new vehicles?
        if(weatherOptimizationMode == WeatherOptimizationModes.Normal){

        } else {
            System.out.println("A mode is currently running.");
        } */
    }


    @Scheduled(initialDelay = 120000, fixedRate = 300000)
    private void monitorSessions() {
        for (Session session : sessions) {
            //modbusConnector.checkSessions(session);
        }
    }

    @Scheduled(initialDelay = 2000, fixedRate = 300000)
    private void updateSolarPower() {
        this.availableSolarPower = 23000.0; //simulation.getSolar().hourOutput();
    }

    private void pauseChargingProcessBySystem(Session session){
        session.setTemporaryPausedBySystem(true);
        simulation.getChargingPoint(session.getLoadingport().getPort()).stopCharging();
        sessionRepository.save(session);
    }
    
    private void pauseChargingProcessByUser(Session session){
        session.setTemporaryPausedByUser(true);
        simulation.getChargingPoint(session.getLoadingport().getPort()).stopCharging();
        notifyUser("Charging Process successfully paused. ");
        sessionRepository.save(session);
    }

    private void createSessionChange(Session session){
        SessionChanges sessionChanges = new SessionChanges(session);
        session.getSessionChanges().add(sessionChanges);
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

}
