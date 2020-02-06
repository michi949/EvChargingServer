package at.fhooe.mc.server.Interfaces;

import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Data.Session;

import java.util.ArrayList;


public interface UpdateOptimizer {

     void updateWeatherForecast(ArrayList<HourlyWeatherForecast> weatherForecasts);

     void addSession(Session session);

     void stopSession(Session session);

     void pauseSession(Session session);

     void restartSession(Session session);
}
