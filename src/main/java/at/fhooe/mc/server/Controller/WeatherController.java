package at.fhooe.mc.server.Controller;
import at.fhooe.mc.server.Data.DailyWeather;
import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import at.fhooe.mc.server.Repository.WeatherForecastRepository;
import at.fhooe.mc.server.Repository.WeatherRepository;
import at.fhooe.mc.server.Services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;

@RestController
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @Autowired
    WeatherForecastRepository weatherForecastRepository;

    @Autowired
    private WeatherRepository weatherRepository;


    @RequestMapping(value = "/updateWeatherForecast", method = RequestMethod.GET)
    public String updateWeatherForecast(@RequestBody String payload) throws Exception {
        weatherService.gatherWeatherDataForecast();
        return "{success:true}";
    }


    @GetMapping(value = "/getForecastWeather", produces = "application/json")
    public HourlyWeatherForecast getForecastWeather() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            ArrayList<HourlyWeatherForecast> weatherForecasts = new ArrayList<>(weatherForecastRepository.findWeatherForecastByTime(calendar.getTime()));

            if(weatherForecasts == null || weatherForecasts.isEmpty()){
                return new HourlyWeatherForecast();
            }

            return weatherForecasts.get(0);
    }
}
