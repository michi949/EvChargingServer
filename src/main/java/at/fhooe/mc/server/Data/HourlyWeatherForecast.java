package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Forecast for the weather. 
 */
@Entity
@Table(name = "hourlyWeatherForecast")
public class HourlyWeatherForecast implements Serializable {
    @Id
    @GeneratedValue
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date requestTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date time;

    double temp;
    int clouds;
    double possiblePower;

    @ManyToOne
    @JoinColumn
    DailyWeather dailyWeather;


    public HourlyWeatherForecast() {
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

    public DailyWeather getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(DailyWeather dailyWeather) {
        this.dailyWeather = dailyWeather;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public double getPossiblePower() {
        return possiblePower;
    }

    public void setPossiblePower(double possiblePower) {
        this.possiblePower = possiblePower;
    }

}
