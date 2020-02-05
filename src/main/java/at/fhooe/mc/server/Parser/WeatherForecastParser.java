package at.fhooe.mc.server.Parser;

import at.fhooe.mc.server.Data.DailyWeather;
import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Repository.WeatherForecastRepository;
import at.fhooe.mc.server.Repository.WeatherRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Parse the Data from JSON String.
 * Usin Jackson Parser, implementet into spring.
 */
@Service
public class WeatherForecastParser {
    DailyWeather dailyWeather;

    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Autowired
    WeatherRepository weatherRepository;

    public void parseWeather(String jsonWeather) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(jsonWeather);
            getOverallDataForTheDay(rootNode.get("city"));
            getWeatherForecastFromArray(rootNode.get("list"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getOverallDataForTheDay(JsonNode dayNode){
        Date currentDate = getDateWithoutTimeUsingCalendar();

        dailyWeather = weatherRepository.findWeatherDataByDay(currentDate);

        if(dailyWeather != null){
            weatherRepository.save(dailyWeather);
            return;
        }

        dailyWeather = new DailyWeather();
        dailyWeather.setLocation(dayNode.get("name").asText());
        long sunrise = dayNode.get("sunrise").asLong() * 1000;
        long sunset = dayNode.get("sunset").asLong() * 1000;

        dailyWeather.setDay(currentDate);
        dailyWeather.setSunrise(new Date(sunrise));
        dailyWeather.setSunset(new Date(sunset));

        dailyWeather.setHourseOfSun(sunset - sunrise);
        weatherRepository.save(dailyWeather);
    }


    private void getWeatherForecastFromArray(JsonNode weatherArray){
        for(int i = 0; i < 7; i++){
            getWeatherForecast(weatherArray.get(i));
        }
    }

    private void getWeatherForecast(JsonNode weatherForecast){
        HourlyWeatherForecast forecast = new HourlyWeatherForecast();
        forecast.setRequestTime(new Date());
        forecast.setTime(new Date(weatherForecast.get("dt").asLong() * 1000));

        JsonNode main = weatherForecast.get("main");
        JsonNode clouds = weatherForecast.get("clouds");

        forecast.setClouds(clouds.get("all").asInt());
        forecast.setTemp(main.get("temp").asDouble());

        checkIfNextDay(forecast);
    }

    private void checkIfNextDay(HourlyWeatherForecast forecast){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(forecast.getTime().getTime() < calendar.getTime().getTime()){
            forecast.setDailyWeather(dailyWeather);
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
