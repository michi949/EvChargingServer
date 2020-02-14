package at.fhooe.mc.server.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Forecast for the weather. 
 */
@Entity
@Table(name = "hourlyWeatherForecast")
public class HourlyWeatherForecast implements Serializable {
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
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
    @JsonIgnore
    DailyWeather dailyWeather;

    boolean isDuringDayLight = false;


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
        checkIfTimeIsDuringDayLight();
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
        checkIfTimeIsDuringDayLight();
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

    public boolean isDuringDayLight() {
        return isDuringDayLight;
    }

    public void setDuringDayLight(boolean duringDayLight) {
        isDuringDayLight = duringDayLight;
    }

    public HourlyWeatherForecast copyWeatherForecast(){
        HourlyWeatherForecast weatherForecast = new HourlyWeatherForecast();
        weatherForecast.setClouds(this.clouds);
        weatherForecast.setDailyWeather(this.dailyWeather);
        weatherForecast.setRequestTime(this.requestTime);
        weatherForecast.setTemp(this.temp);
        weatherForecast.setTime(this.time);
        return weatherForecast;
    }

    public void checkIfTimeIsDuringDayLight(){
            if(this.getTime() == null || this.getDailyWeather() == null){
                this.setDuringDayLight(false);
                return;
            }

            if (this.getDailyWeather().sunrise.compareTo(this.getTime()) * this.getTime().compareTo(this.getDailyWeather().sunset) > 0) {
                this.setDuringDayLight(true);
             } else {
                this.setDuringDayLight(false);
            }
    }

    public String getTimestampAsStringForHtml(){
        return convertDateToString(this.time);
    }

    private String convertDateToString(Date date){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
    }
}
