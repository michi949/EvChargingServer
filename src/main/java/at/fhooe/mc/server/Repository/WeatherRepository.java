package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Weather connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface WeatherRepository extends CrudRepository<Weather, Integer> {

    @Query("SELECT u FROM Weather u WHERE u.day = ?1")
    Weather findWeatherDataByDay(Date day);

}
