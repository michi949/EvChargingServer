package at.fhooe.mc.server.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "session")
public class Session implements Serializable {
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    int id;
    @Temporal(TemporalType.TIMESTAMP)
    Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate;
    Double restCapacity;
    Double endCapacity;
    Double currentCapacity;
    Double optimizedPower; //The amount of energie which is optimized from the charging power.
    Double minPower; //The rating and the minimum power
    Double chargingPower; //Power applied to the station
    boolean isOptimized;
    boolean isSlowMode;
    boolean isFallBack;
    double timeToEnd;
    boolean isTemporaryPausedByUser = false;
    boolean isTemporaryPausedBySystem = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @JsonIgnore
    Car car;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(unique = true)
    LoadingPort loadingport;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<SessionChanges> sessionChanges = new HashSet<>();

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

    public Double getChargingPower() {
        return chargingPower;
    }

    public void setChargingPower(Double chargingPower) {
        this.chargingPower = chargingPower;
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

    public Double getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(Double currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Set<SessionChanges> getSessionChanges() {
        return sessionChanges;
    }

    public void setSessionChanges(Set<SessionChanges> sessionChanges) {
        this.sessionChanges = sessionChanges;
    }

    public double getCurrentCapacityForHTML(){
        return round(currentCapacity, 2);
    }

    public double getGoalCapacityForHTML(){
        return round(endCapacity, 2);
    }

    public double getMinPowerForHTML(){
        return round(minPower, 2);
    }

    public double getOptimizedPowerForHTML(){
        return round(optimizedPower, 2);
    }

    public double getChargingPowerForHTML(){
        return round(chargingPower, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
