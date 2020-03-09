package at.fhooe.mc.server.Simulation;


import ChargingEnviroment.Enums.EvSimChargingType;
import ChargingEnviroment.EvSimBattery;
import ChargingEnviroment.EvSimChargingPoint;
import ChargingEnviroment.EvSimChargingStation;
import ChargingEnviroment.EvSimVehicle;
import EnergySources.EvSimSolar;
import Factory.HagenbergSimulationFactory;
import at.fhooe.mc.server.Data.*;
import at.fhooe.mc.server.Repository.WeatherForecastRepository;
import at.fhooe.mc.server.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class Simulation {
    ArrayList<EvSimChargingStation> chargingStations = new ArrayList<>();
    EvSimSolar solar;

    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Autowired
    WeatherRepository weatherRepository;

    public Simulation() {
        chargingStations = HagenbergSimulationFactory.setupEnvironmentHagenberg();
        solar = new EvSimSolar(530000, 0.45, 26.0, true);
    }

    public ArrayList<EvSimChargingStation> getChargingStations() {
        return chargingStations;
    }

    public void setChargingStations(ArrayList<EvSimChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
    }

    public EvSimSolar getSolar() {
        return solar;
    }

    public void setSolar(EvSimSolar solar) {
        this.solar = solar;
    }

    public EvSimChargingPoint getChargingPoint(int point){
        for(EvSimChargingStation station : chargingStations){
           for(EvSimChargingPoint attachedPoint : station.getEvSimChargingPoints()){
               if(attachedPoint.getId() == point){
                   return attachedPoint;
               }
           }
        }
        return null;
    }

    public void setVehicleToChargingPoint(Session session){
        LoadingPort port = session.getLoadingport();
        Car car = session.getCar();

        EvSimChargingPoint simulationPoint = getChargingPoint(port.getPort());

        if(simulationPoint == null){
            return;
        }

        double currentCapacity = 0;
        if(session.getCurrentCapacity() ==  null || session.getCurrentCapacity() == 0) {
             currentCapacity = Math.random() * (car.getCapacity() - 3700.0D + 1.0D) + 3700.0D;
        } else {
             currentCapacity = session.getCurrentCapacity();
        }
        session.setCurrentCapacity(currentCapacity);
        EvSimVehicle simulationVehicle =  new EvSimVehicle(new EvSimBattery(currentCapacity, car.getCapacity()), car.getType(), EvSimChargingType.AC);


        simulationPoint.addVehicleToPoint(simulationVehicle);
    }

    public void setupWeatherExample(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 01);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 00);
        Date startDay = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 24);
        Date endDay = calendar.getTime();

        ArrayList<HourlyWeatherForecast> weatherForecasts = new ArrayList<>(weatherForecastRepository.findNextWeatherForecasts(startDay, endDay));
        deleteCurrentWeatherForecast(weatherForecasts);

    }

    public void setupGoodCaseWeahter(){
        DailyWeather dailyWeather = new DailyWeather();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        dailyWeather.setDay(calendar.getTime());
        dailyWeather.setLocation("Hagengberg");

        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 24);
        calendar.set(Calendar.SECOND, 56);
        Date sunrise = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 42);
        calendar.set(Calendar.SECOND, 01);
        Date sunset = calendar.getTime();

        dailyWeather.setSunrise(sunrise);
        dailyWeather.setSunset(sunset);
        dailyWeather.setHourseOfSun(sunset.getTime() - sunrise.getTime());

        weatherRepository.save(dailyWeather);
    }

    public void deleteCurrentWeatherForecast(ArrayList<HourlyWeatherForecast> weatherForecasts){
        DailyWeather dailyWeather = weatherForecasts.get(0).getDailyWeather();
        for(HourlyWeatherForecast weatherForecast: weatherForecasts){
            weatherForecastRepository.delete(weatherForecast);
        }
        weatherRepository.delete(dailyWeather);
    }
}
