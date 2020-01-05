package at.fhooe.mc.server.Services.Optimizer;

import at.fhooe.mc.server.Connector.ModbusConnector;
import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;


@Service
public class Optimizer implements Runnable, UpdateOptimizer {
    ModbusConnector modbusConnector = new ModbusConnector();
    ArrayList<Session> sessions = new ArrayList<>();
    Double availableSolarPower = 0.0;

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

    private void readjustSessions() {

    }

    private void getRunningSessions(){

    }

    private void divideAvailableSolarPower(){

    }

    private double calculateRatingForSession(Session session){

        return 0.0;
    }

    private void calculateRatingForAllSessions(){

    }

    private double calculateMinimumPower(Session session){

        return 0.0;
    }

    private double getCurrentCapacityForSession(Session session){

        return 0.0;
    }

    private double getAvailableSolarPower(){

        return 0.0;
    }

    private double calculateLeftOverTime(Session session){

        return 0.0;
    }

    private double calculateLeftOverCapacity(Session session){
        Double currentCapacity = getCurrentCapacityForSession(session);
        return session.getEndCapacity() - currentCapacity;
    }
}
