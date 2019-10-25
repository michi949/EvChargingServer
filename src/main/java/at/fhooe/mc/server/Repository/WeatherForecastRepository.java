package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Weather;
import at.fhooe.mc.server.Data.WeatherForecast;
import org.springframework.data.repository.CrudRepository;

/**
 * WeatherForecast connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface WeatherForecastRepository extends CrudRepository<WeatherForecast, Integer> {

}
