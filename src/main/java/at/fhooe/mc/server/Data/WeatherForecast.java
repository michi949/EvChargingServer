package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Forecast for the weather. 
 */
@Entity
@Table(name = "weatherForecast")
public class WeatherForecast implements Serializable {
    @Id
    @GeneratedValue
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date requestTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date time;

    double temp;
    int clouds;

    @ManyToOne
    @JoinColumn
    Weather weather;

    public WeatherForecast() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
}
