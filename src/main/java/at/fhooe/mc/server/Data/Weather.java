package at.fhooe.mc.server.Data;

public class Weather {
    String location;
    double temperature;
    int pressure;
    double temperature_max;
    double temperature_min;
    int clouds;
    int sunrise;
    int sunset;
    String light;

    public Weather() {
        location = "Hagenberg";
        temperature = 0.0;
        pressure = 0;
        temperature_max = 0.0;
        temperature_min = 0.0;
        clouds = 0;
        sunrise = 0;
        sunset = 0;
        light = "light";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getTemperature_max() {
        return temperature_max;
    }

    public void setTemperature_max(double temperature_max) {
        this.temperature_max = temperature_max;
    }

    public double getTemperature_min() {
        return temperature_min;
    }

    public void setTemperature_min(double temperature_min) {
        this.temperature_min = temperature_min;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }
}
