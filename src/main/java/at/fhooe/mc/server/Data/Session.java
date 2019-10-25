package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "session")
public class Session implements Serializable {
    @Id
    @GeneratedValue
    int id;
    @Temporal(TemporalType.TIMESTAMP)
    Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate;
    int currentPercent;
    int power;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    LoadingPort loadingport;

    public Session() {
        id = 0;
        startDate = new Date();
        endDate = new Date();
        currentPercent = 0;
        power = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getCurrentPercent() {
        return currentPercent;
    }

    public void setCurrentPercent(int currentPercent) {
        this.currentPercent = currentPercent;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LoadingPort getLoadingport() {
        return loadingport;
    }

    public void setLoadingport(LoadingPort loadingport) {
        this.loadingport = loadingport;
    }
}
