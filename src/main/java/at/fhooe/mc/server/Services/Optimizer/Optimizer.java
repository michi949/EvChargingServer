package at.fhooe.mc.server.Services.Optimizer;

import at.fhooe.mc.server.Connector.ModbusConnector;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.SimulationInterface;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Repository.LoadingPortRepository;
import at.fhooe.mc.server.Repository.SessionRepository;
import at.fhooe.mc.server.Simulation.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class Optimizer implements Runnable, UpdateOptimizer {
    //ModbusConnector modbusConnector = new ModbusConnector();
    ArrayList<Session> sessions = new ArrayList<>();
    ArrayList<Weather> weathers = new ArrayList<>();
    Double availableSolarPower = 0.0;
    public SimulationInterface simulationInterface;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public void run() {
        System.out.println("Optimizer Started!");
    }


    @Override
    public void updateCurrentWeather(Weather weather) {

    }

    @Override
    public void updateForecastWeather(Weather weather) {

    }

    @Override
    public void addSession() {
            System.out.println("");
    }

    private void getRunningSessions(){

    }

    private void divideAvailableSolarPower(){
        availableSolarPower = getAvailableSolarPower();
        sortSessionsAfterRating();

        if(!sessions.isEmpty()){
            adjustSessions(0, availableSolarPower);
        }
    }

    private void adjustSessions(int pos, double availableSolarPower) {
        Session session = sessions.get(pos);

        if (session == null) {
            if(availableSolarPower != 0.0){
                adjustFallBackSessions(availableSolarPower);
            }
            return;
        }

        if(!session.isFallBack()){
            if(availableSolarPower < 3.7){
                session.setOptimized(false);
                applyToChargingPoint(session, session.getMinPower());
            } else {
                if(session.getMinPower() > 21.0 && session.getMinPower() <= 22.0){
                    session.setOptimized(true);
                    availableSolarPower -= session.getMinPower();
                    session.setOptimizedPower(session.getMinPower());
                    adjustSessions(pos += 1, availableSolarPower);
                } else if (session.getMinPower() < 3.7 && session.isOptimized()) {
                    session.setOptimized(false);
                    pauseChargingProcess();
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
            applyToChargingPoint(session, 3.7);
        }
    }

    private Map<Integer, Double> splitAvailableSolarPower(double availableSolarPower){
        double forSession = availableSolarPower * 0.65;
        double leftOverForOthers = 0.0;

        if(forSession < 3.7){
            forSession = availableSolarPower;
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
       if(solarPowerForEachSession > 3.7){
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

    private void pauseChargingProcess(){

    }

    private void applyToChargingPoint(Session session, double power){

    }

    private void sortSessionsAfterRating(){
        sessions.sort((Session p1, Session p2) -> p1.getRating().compareTo(p2.getRating()));
    }

    private void calculateRatingForAllSessions(){
        for(Session session : sessions){
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
            if (session.getMinPower() > 3.7) {
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
            return;
        }

        if(!session.isSlowMode()){
            if(session.getMinPower() > 22){
                notifyUser("Optimaztion not possible in given Time!");
                moveSessionToFallBack(session);
            }
        } else {
            if(session.getMinPower() > 11){
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
        session.setMinPower(3.7);
        session.setFallBack(true);
    }

    private double getCurrentCapacityForSession(Session session){

        return 0.0;
    }

    private double getAvailableSolarPower(){

        return 0.0;
    }

    private long calculateLeftOverTime(Session session){
        Date dateWithPuffer = new Date(session.getEndDate().getTime() - TimeUnit.MINUTES.toMillis(30));
        long diffInMillies = Math.abs(dateWithPuffer.getTime() - new Date().getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
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

    private Session getSessionToPort(){


        return new Session();
    }

}
