package at.fhooe.mc.server.Controller;
import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Data.Weather;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    WeatherConnector connector = new WeatherConnector();


    @GetMapping(value = "/getCurrentWeather", produces = "application/json")
    public Weather getCurrentWeather() {
        Weather weather = connector.peformRequest();
        return weather;
    }


    @GetMapping(value = "/getForecastWeather", produces = "application/json")
    public Weather getForecastWeather() {


        return new Weather();
    }
}
