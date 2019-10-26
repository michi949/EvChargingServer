package at.fhooe.mc.server.Interfaces;

import at.fhooe.mc.server.Data.Weather;

public interface UpdateWeather {

     void updateCurrentWeather(Weather weather);

     void updateForecastWeather(Weather weather);

}
