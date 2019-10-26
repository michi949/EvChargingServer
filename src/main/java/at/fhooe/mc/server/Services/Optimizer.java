package at.fhooe.mc.server.Services;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.UpdateWeather;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Optimizer implements Runnable, UpdateWeather {


    @Override
    public void run() {
        System.out.println("Optimizer Started!");
    }

    @Override
    public void updateCurrentWeather(Weather weather) {
        System.out.println("Weather Updated.");
    }

    @Override
    public void updateForecastWeather(Weather weather) {

    }
}
