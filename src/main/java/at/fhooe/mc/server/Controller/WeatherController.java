package at.fhooe.mc.server.Controller;
import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WeatherController {
    @Autowired
    WeatherConnector weatherConnector;

    @Autowired
    private WeatherRepository weatherRepository;


    @RequestMapping(value = "/updateWeatherForecast", method = RequestMethod.GET)
    public String updateWeatherForecast(@RequestBody String payload) throws Exception {
        weatherConnector.peformRequest();
        return "{success:true}";
    }


        @GetMapping(value = "/getForecastWeather", produces = "application/json")
    public Weather getForecastWeather() {

        return new Weather();
    }
}
