package at.fhooe.mc.server.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "solarPanels")
public class SolarPanels implements Serializable {
    @Id
    int id;

    double wattPeak;
    double temperatureCoefficient;
    double cellTemperature;

/**
 * - monokristallines Silizium -0,40%/K
 * - polykristallines Silizium -0,45%/K
 * - Cadmium-Telluid           -0,20%/K
 * - amorphes Silizium         -0,40%/K */
    public SolarPanels() {
        id = 1;
        wattPeak = 53000;
        temperatureCoefficient = -0.40;
        cellTemperature = 26;
    }


    public double getPossiblePowerForDay(double currentTemperature, double sunnyHours){
        double bestCase = wattPeak * sunnyHours;

        double difference = differenceBetweenTemperature(currentTemperature);
        double percent = temperatureCoefficient * Math.abs(difference);

        if(difference > 0){
            return (bestCase / 100) * (100 - percent);
        } else {
            return (bestCase / 100) * (100 + percent);
        }
    }

    public double getPossiblePowerForHour(double currentTemperature){
        double difference = differenceBetweenTemperature(currentTemperature);
        double percent = temperatureCoefficient * Math.abs(difference);
        if(difference > 0){
            return (wattPeak / 100) * (100 - percent);
        } else {
            return (wattPeak / 100) * (100 + percent);
        }
    }

    private double differenceBetweenTemperature(double currentTemperature){
        BigDecimal bd = BigDecimal.valueOf((currentTemperature - cellTemperature));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getWattPeak() {
        return wattPeak;
    }

    public void setWattPeak(double wattPeak) {
        this.wattPeak = wattPeak;
    }

    public double getTemperatureCoefficient() {
        return temperatureCoefficient;
    }

    public void setTemperatureCoefficient(double temperatureCoefficient) {
        this.temperatureCoefficient = temperatureCoefficient;
    }

    public double getCellTemperature() {
        return cellTemperature;
    }

    public void setCellTemperature(double cellTemperature) {
        this.cellTemperature = cellTemperature;
    }

    @Override
    public String toString() {
        return "SolarPanels{" +
                "id=" + id +
                ", wattPeak=" + wattPeak +
                ", temperatureCoefficient=" + temperatureCoefficient +
                ", cellTemperature=" + cellTemperature +
                '}';
    }
}
