package at.fhooe.mc.server.Interfaces;

import at.fhooe.mc.server.Data.Weather;
import org.springframework.stereotype.Component;

@Component
public interface UpdateOptimizer {

     void updateCurrentWeather(Weather weather);

     void updateForecastWeather(Weather weather);

     void addSession();
}
