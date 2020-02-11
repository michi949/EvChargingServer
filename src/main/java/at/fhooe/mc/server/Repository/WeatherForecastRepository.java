package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.HourlyWeatherForecast;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface WeatherForecastRepository extends CrudRepository<HourlyWeatherForecast, Integer> {

    @Query("SELECT u FROM HourlyWeatherForecast u WHERE u.time = ?1")
    Set<HourlyWeatherForecast> findWeatherForecastByTime(Date time);

    @Query("SELECT u FROM HourlyWeatherForecast u WHERE u.time > ?1 AND u.time < ?2")
    Set<HourlyWeatherForecast> findNextWeatherForecasts(Date startTime, Date endTime);

}