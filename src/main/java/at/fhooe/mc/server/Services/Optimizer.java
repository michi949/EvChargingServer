package at.fhooe.mc.server.Services;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;


public class Optimizer implements Runnable, UpdateOptimizer {

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

    }
}
