package at.fhooe.mc.server.Services;


import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Download the weather for ervery hour.
 */
@Component
public class WeatherService  {
    UpdateOptimizer updateOptimizer;
    WeatherConnector weatherConnector;

    @Autowired
    WeatherRepository weatherRepository;

    public WeatherService(UpdateOptimizer updateOptimizer) {
        this.updateOptimizer = updateOptimizer;
        weatherConnector = new WeatherConnector();
    }

    // 0 0 */1 * * *  every hour.
    @Scheduled(cron = "0 0 */1 * * *")
    public void gatherData() {
        Weather weather = weatherConnector.peformRequest();

        if (weather != null) {
            weatherRepository.save(weather);
            updateOptimizer.updateCurrentWeather(weather);
        }
    }

}
