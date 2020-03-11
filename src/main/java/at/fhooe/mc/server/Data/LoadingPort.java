package at.fhooe.mc.server.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "loadingPort")
public class LoadingPort implements Serializable {
    @Id
    int port;
    boolean occupied;
    String ip;

    @OneToOne(mappedBy = "loadingPort")
    @JsonIgnore
    Session session;

    @OneToMany(mappedBy = "loadingPort", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Reservation> reservation;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    LoadingStation loadingStation;

    public LoadingPort() {
    }

    public LoadingPort(int port) {
        this.port = port;
        session = null;
        reservation = null;
        occupied = false;
        ip = "";
    }

    public LoadingPort(int port, String ip) {
        this.port = port;
        session = null;
        reservation = null;
        occupied = false;
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @JsonIgnore
    public Set<Reservation> getReservations() {
        return reservation;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservation = reservations;
    }

    public LoadingStation getLoadingStation() {
        return loadingStation;
    }

    public void setLoadingStation(LoadingStation loadingstation) {
        this.loadingStation = loadingstation;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
