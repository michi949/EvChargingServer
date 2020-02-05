package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.DailyWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Weather connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface WeatherRepository extends CrudRepository<DailyWeather, Integer> {

    @Query("SELECT u FROM DailyWeather u WHERE u.day = ?1")
    DailyWeather findWeatherDataByDay(Date day);

}
