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
    int power;
    Double restCapacity;
    Double endCapacity;
    Double optimizedPower;
    Double minPower;
    boolean isOptimized;
    boolean isSlowMode;
    boolean isFallBack;
    double timeToEnd;
    boolean isTemporaryPausedByUser = false;
    boolean isTemporaryPausedBySystem = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    LoadingPort loadingport;

    public Session(){}

    public Session(Date endDate, Double endCapacity, boolean isSlowMode, Car car, LoadingPort loadingport) {
        this.endDate = endDate;
        this.endCapacity = endCapacity;
        this.isSlowMode = isSlowMode;
        this.car = car;
        this.loadingport = loadingport;
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

    public Double getRestCapacity() {
        return restCapacity;
    }

    public void setRestCapacity(Double restCapacity) {
        this.restCapacity = restCapacity;
    }

    public Double getEndCapacity() {
        return endCapacity;
    }

    public void setEndCapacity(int capacityInPercent) {
        this.endCapacity = car.getCapacity() * (capacityInPercent / 100);
    }

    public double getTimeToEnd() {
        return timeToEnd;
    }

    public void setTimeToEnd(double timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public Double getOptimizedPower() {
        return optimizedPower;
    }

    public void setOptimizedPower(Double optimizedPower) {
        this.optimizedPower = optimizedPower;
    }

    public Double getMinPower() {
        return minPower;
    }

    public void setMinPower(Double minPower) {
        this.minPower = minPower;
    }

    public boolean isOptimized() {
        return isOptimized;
    }

    public void setOptimized(boolean optimized) {
        isOptimized = optimized;
    }

    public boolean isSlowMode() {
        return isSlowMode;
    }

    public void setSlowMode(boolean slowMode) {
        isSlowMode = slowMode;
    }

    public boolean isFallBack() {
        return isFallBack;
    }

    public void setFallBack(boolean fallBack) {
        isFallBack = fallBack;
    }

    public void setEndCapacity(Double endCapacity) {
        this.endCapacity = endCapacity;
    }

    public boolean isTemporaryPausedByUser() {
        return isTemporaryPausedByUser;
    }

    public void setTemporaryPausedByUser(boolean temporaryPausedByUser) {
        isTemporaryPausedByUser = temporaryPausedByUser;
    }

    public boolean isTemporaryPausedBySystem() {
        return isTemporaryPausedBySystem;
    }

    public void setTemporaryPausedBySystem(boolean temporaryPausedBySystem) {
        isTemporaryPausedBySystem = temporaryPausedBySystem;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", power=" + power +
                ", restCapacity=" + restCapacity +
                ", endCapacity=" + endCapacity +
                ", optimizedPower=" + optimizedPower +
                ", minPower=" + minPower +
                ", isOptimized=" + isOptimized +
                ", isSlowMode=" + isSlowMode +
                ", isFallBack=" + isFallBack +
                ", timeToEnd=" + timeToEnd +
                ", isTemporaryPausedByUser=" + isTemporaryPausedByUser +
                ", isTemporaryPausedBySystem=" + isTemporaryPausedBySystem +
                ", car=" + car +
                ", loadingport=" + loadingport +
                '}';
    }
}
