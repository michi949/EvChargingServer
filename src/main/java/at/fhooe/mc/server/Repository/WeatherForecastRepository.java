package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Data.WeatherForecast;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherForecastRepository extends CrudRepository<WeatherForecast, Integer> {

}