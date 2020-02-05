package at.fhooe.mc.server.Interfaces;

import at.fhooe.mc.server.Data.Session;


public interface UpdateOptimizer {

     void updateWeatherForecast();

     void addSession(Session session);

     void removeSession(Session session);
}
