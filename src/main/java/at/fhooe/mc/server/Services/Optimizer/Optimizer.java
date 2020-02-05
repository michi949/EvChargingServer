package at.fhooe.mc.server.Services.Optimizer;

import ChargingEnviroment.EvSimChargingPoint;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.DailyWeather;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Repository.SessionRepository;
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
    Double availableSolarPower = 0.0;
    Simulation simulation = new Simulation();

    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    WeatherForecastRepository weatherForecastRepository;
    @Autowired
    WeatherRepository weatherRepository;

    @Override
    public void run() {
        System.out.println("Optimizer Started!");
    }


    @Override
    public void updateWeatherForecast() {
            checkWeatherForecast();
            estimateForecastSolarPower();
            updateCurrentWeather();
            calculateRatingForAllSessions();
    }

    @Override
    public void addSession(Session session) {
        this.sessions.add(session);
        session.setStartDate(new Date());
        simulation.setVehicleToChargingPoint(session);
        simulation.getChargingPoint(session.getLoadingport().getPort()).startCharging();
        calculateRatingForAllSessions();
    }

    @Override
    public void removeSession(Session session) {
        this.sessions.remove(session);
        sessionRepository.delete(session);
        simulation.getChargingPoint(session.getLoadingport().getPort()).stopCharging();
        simulation.getChargingPoint(session.getLoadingport().getPort()).removeVehicleFromPoint();
        calculateRatingForAllSessions();
    }


    private void divideAvailableSolarPower(){
        availableSolarPower = getAvailableSolarPower();
        sortSessionsAfterRating();

        if(!sessions.isEmpty()){
            adjustSessions(0, availableSolarPower);
        }
    }

    private void adjustSessions(int pos, double availableSolarPower) {

        if (sessions.size() <= pos) {
            if(availableSolarPower != 0.0){
                adjustFallBackSessions(availableSolarPower);
            }
            return;
        }

        Session session = sessions.get(pos);

        if(!session.isFallBack()){
            if(availableSolarPower < 3700){
                session.setOptimized(false);
                applyToChargingPoint(session, session.getMinPower());
            } else {
                if(session.getMinPower() > 21000 && session.getMinPower() <= 22000){
                    session.setOptimized(true);
                    availableSolarPower -= session.getMinPower();
                    session.setOptimizedPower(session.getMinPower());
                    adjustSessions(pos += 1, availableSolarPower);
                } else if (session.getMinPower() < 3700 && session.isOptimized()) {
                    session.setOptimized(false);
                    //pauseChargingProcess();
                    adjustSessions(pos += 1, availableSolarPower);
                } else {
                    Map<Integer, Double> map = splitAvailableSolarPower(availableSolarPower);
                    session.setOptimized(true);
                    session.setOptimizedPower(map.get(0));
                    applyToChargingPoint(session, session.getOptimizedPower());
                    adjustSessions(pos += 1, map.get(1));
                }
            }
        } else {
            applyToChargingPoint(session, 3700);
        }
    }

    private Map<Integer, Double> splitAvailableSolarPower(double availableSolarPower){
        double forSession = availableSolarPower * 0.65;
        double leftOverForOthers = 0.0;

        if(forSession < 3700.0){
            forSession = availableSolarPower;
        } else if(forSession > 22000.0){
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

    private void adjustFallBackSessions(double leftOverSolarPower){
       double solarPowerForEachSession = leftOverSolarPower / countFallBackSessions();
       if(solarPowerForEachSession > 3700){
           for(Session session : sessions){
               if(session.isFallBack()){
                   session.setOptimized(true);
                   session.setOptimizedPower(solarPowerForEachSession);
                   applyToChargingPoint(session, solarPowerForEachSession);
               }
           }
       }
    }

    private int countFallBackSessions(){
        int count = 0;
        for(Session session : sessions){
            if(session.isFallBack()){
                count += 1;
            }
        }
        return count;
    }

    private void applyToChargingPoint(Session session, double power){
        EvSimChargingPoint simulationPoint = simulation.getChargingPoint(session.getLoadingport().getPort());
        simulationPoint.changeChargingSpeedOnPoint(power);
        sessionRepository.save(session);
    }

    private void sortSessionsAfterRating(){
        sessions.sort((Session p1, Session p2) -> p1.getRating().compareTo(p2.getRating()));
    }

    private void calculateRatingForAllSessions(){
        for(Session session : sessions){
            calculateLeftOverCapacity(session);
            calculateLeftOverTime(session);
            calculateRatingForSession(session);
            calculateMinimumPower(session);
        }
        divideAvailableSolarPower();
    }

    private void calculateRatingForSession(Session session){
        Double rating = session.getRestCapacity() / (session.getTimeToEnd() / 100);
        session.setRating(rating);
    }

    private void calculateMinimumPower(Session session){
        Double minPower = session.getRestCapacity() / session.getTimeToEnd();
        session.setMinPower(minPower);
        checkMinimumPower(session);
    }

    private void checkMinimumPower(Session session){
        if(session.getCar().isOnePhase()){
            if (session.getMinPower() > 3700) {
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
            return;
        }

        if(!session.isSlowMode()){
            if(session.getMinPower() > 22000){
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
        } else {
            if(session.getMinPower() > 11000){
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
        }
    }

    private void notifyUser(String msg){
        System.out.println(msg);
    }

    private void moveSessionToFallBack(Session session){
        session.setRating(0.0);
        session.setMinPower(3700.0);
        session.setFallBack(true);
    }

    private double getCurrentCapacityForSession(Session session){
        return simulation.getChargingPoint(session.getLoadingport().getPort()).getEvSimVehicle().getEvSimBattery().getCurrentCapacity();
    }

    private double getAvailableSolarPower(){
        return simulation.getSolar().hourOutput();
    }

    private double calculateLeftOverTime(Session session){
        long tmp = session.getEndDate().getTime() - new Date().getTime();
        Date dateWithPuffer = new Date(tmp);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateWithPuffer);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        String dateString = hours+"."+minutes+seconds;
        double diff = Double.parseDouble(dateString);
        session.setTimeToEnd(diff);

        return diff;
    }

    private double calculateLeftOverCapacity(Session session){
        Double currentCapacity = getCurrentCapacityForSession(session);
        Double diff = session.getEndCapacity() - currentCapacity;
        session.setRestCapacity(diff);
        return diff;
    }

    @Scheduled(initialDelay=120000, fixedRate=300000)
    private void monitorSessions(){
        for(Session session : sessions){
            //modbusConnector.checkSessions(session);
        }
    }

    private void validateSessions(int port, double capacity){

    }

    private void checkWeatherForecast(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        DailyWeather dailyWeather = weatherRepository.findWeatherDataByDay(calendar.getTime());

    }

    private void estimateForecastSolarPower(){

    }

    private void updateCurrentWeather(){

    }


}
