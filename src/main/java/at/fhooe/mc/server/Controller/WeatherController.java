package at.fhooe.mc.server.Controller;
import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    WeatherConnector connector = new WeatherConnector();

    @Autowired
    private WeatherRepository weatherRepository;

    @GetMapping(value = "/getCurrentWeather", produces = "application/json")
    public Weather getCurrentWeather() {
        Weather weather = weatherRepository.findOne((int) weatherRepository.count());

        if(weather != null) {
            return weather;
        } else {
            weatherRepository.save(connector.peformRequest());
            weather = weatherRepository.findOne((int) weatherRepository.count());
        }

        return weather;
    }


    @GetMapping(value = "/getForecastWeather", produces = "application/json")
    public Weather getForecastWeather() {

        return new Weather();
    }
}
