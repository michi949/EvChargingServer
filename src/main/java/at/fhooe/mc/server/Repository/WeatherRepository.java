package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Weather;
import org.springframework.data.repository.CrudRepository;

/**
 * Weather connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface WeatherRepository extends CrudRepository<Weather, Integer> {

}
