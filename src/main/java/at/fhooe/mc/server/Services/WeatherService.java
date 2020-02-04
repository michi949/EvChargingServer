package at.fhooe.mc.server.Services;


import at.fhooe.mc.server.Connector.WeatherConnector;
import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Interfaces.UpdateOptimizer;
import at.fhooe.mc.server.Repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Download the weather for ervery hour.
 */
@Service
public class WeatherService  {
    UpdateOptimizer updateOptimizer;
    WeatherConnector weatherConnector;

    @Autowired
    WeatherRepository weatherRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public WeatherService(UpdateOptimizer updateOptimizer) {
        this.updateOptimizer = updateOptimizer;
        weatherConnector = new WeatherConnector();
    }

    // 0 0 */1 * * *  every hour.
    //Cron Job, to get data from api.
    @Scheduled(cron = "0 0 */1 * * *")
    public void gatherWeatherDataForecast() {
        Weather weather = weatherConnector.peformRequest();

        if (weather != null) {
            weatherRepository.save(weather);
            updateOptimizer.updateCurrentWeather(weather);
        }
    }

}
