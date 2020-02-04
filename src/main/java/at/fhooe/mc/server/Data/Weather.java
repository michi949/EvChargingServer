package at.fhooe.mc.server.Data;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "weather")
public class Weather implements Serializable {
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

    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL)
    Set<WeatherForecast> weatherForecasts;


    public Weather() {
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

    public Set<WeatherForecast> getWeatherForecasts() {
        return weatherForecasts;
    }

    public void setWeatherForecasts(Set<WeatherForecast> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    public long getHourseOfSun() {
        return hourseOfSun;
    }

    public void setHourseOfSun(long hourseOfSun) {
        this.hourseOfSun = hourseOfSun;
    }
}
