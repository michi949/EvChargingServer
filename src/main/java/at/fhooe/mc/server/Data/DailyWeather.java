package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "dailyWeather")
public class DailyWeather implements Serializable {
    @Id
    @GeneratedValue
    int id;
    String location;
    @Temporal(TemporalType.TIMESTAMP)
    Date day;
    @Temporal(TemporalType.TIMESTAMP)
    Date sunrise;
    @Temporal(TemporalType.TIMESTAMP)
    Date sunset;
    long hourseOfSun;

    @OneToMany(mappedBy = "dailyWeather", cascade = CascadeType.ALL)
    Set<HourlyWeatherForecast> hourlyWeatherForecasts;


    public DailyWeather() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getSunrise() {
        return sunrise;
    }

    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }

    public Set<HourlyWeatherForecast> getHourlyWeatherForecasts() {
        return hourlyWeatherForecasts;
    }

    public void setHourlyWeatherForecasts(Set<HourlyWeatherForecast> hourlyWeatherForecasts) {
        this.hourlyWeatherForecasts = hourlyWeatherForecasts;
    }

    public long getHourseOfSun() {
        return hourseOfSun;
    }

    public void setHourseOfSun(long hourseOfSun) {
        this.hourseOfSun = hourseOfSun;
    }

}
