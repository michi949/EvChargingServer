package at.fhooe.mc.server.Parser;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Data.WeatherForecast;
import at.fhooe.mc.server.Repository.WeatherForecastRepository;
import at.fhooe.mc.server.Repository.WeatherRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Parse the Data from JSON String.
 * Usin Jackson Parser, implementet into spring.
 */
@Service
public class WeatherForecastParser {
    Weather weather;

    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Autowired
    WeatherRepository weatherRepository;

    public  Weather parseWeather(String jsonWeather) {

        Weather weather = new Weather();
        ObjectMapper mapper = new ObjectMapper();

        try {

            JsonNode rootNode = mapper.readTree(jsonWeather);
            getOverallDataForTheDay(rootNode.get("city"));
            getWeatherForecastFromArray(rootNode.get("list"));

        } catch (IOException e) {
            e.printStackTrace();
            return new Weather();
        }

        return weather;
    }


    private void getOverallDataForTheDay(JsonNode dayNode){
        Date currentDate = getDateWithoutTimeUsingCalendar();

        weather = weatherRepository.findWeatherDataByDay(currentDate);

        if(weather != null){
            return;
        }

        weather = new Weather();
        weather.setLocation(dayNode.get("name").asText());
        long sunrise = dayNode.get("sunrise").asLong() * 1000;
        long sunset = dayNode.get("sunset").asLong() * 1000;

        weather.setDay(currentDate);
        weather.setSunrise(new Date(sunrise));
        weather.setSunset(new Date(sunset));

        weather.setHourseOfSun(sunset - sunrise);
        weatherRepository.save(weather);
    }


    private void getWeatherForecastFromArray(JsonNode weatherArray){
        for(int i = 0; i < 7; i++){
            getWeatherForecast(weatherArray.get(i));
        }
    }

    private void getWeatherForecast(JsonNode weatherForecast){
        WeatherForecast forecast = new WeatherForecast();
        forecast.setRequestTime(new Date());
        forecast.setTime(new Date(weatherForecast.get("dt").asLong() * 1000));

        JsonNode main = weatherForecast.get("main");
        JsonNode clouds = weatherForecast.get("clouds");

        forecast.setClouds(clouds.get("all").asInt());
        forecast.setTemp(main.get("temp").asDouble());

        checkIfNextDay(forecast);
    }

    private void checkIfNextDay(WeatherForecast forecast){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(forecast.getTime().getTime() < calendar.getTime().getTime()){
            forecast.setWeather(weather);
            weatherForecastRepository.save(forecast);
        }
    }

    public static Date getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
}
