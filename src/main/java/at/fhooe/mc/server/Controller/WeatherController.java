package at.fhooe.mc.server.Controller;
import at.fhooe.mc.server.Data.DailyWeather;
import at.fhooe.mc.server.Repository.WeatherRepository;
import at.fhooe.mc.server.Services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WeatherController {
    @Autowired
    WeatherService weatherService;

    @Autowired
    private WeatherRepository weatherRepository;


    @RequestMapping(value = "/updateWeatherForecast", method = RequestMethod.GET)
    public String updateWeatherForecast(@RequestBody String payload) throws Exception {
        weatherService.gatherWeatherDataForecast();
        return "{success:true}";
    }


        @GetMapping(value = "/getForecastWeather", produces = "application/json")
    public DailyWeather getForecastWeather() {

        return new DailyWeather();
    }
}
