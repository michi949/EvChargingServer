package at.fhooe.mc.server.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "loadingport")
public class LoadingPort {
    @Id
    int port;
    boolean occupied;

    @OneToOne(mappedBy = "loadingport")
    @JsonIgnore
    Session session;

    @OneToMany(mappedBy = "loadingport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Reservation> reservation;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    LoadingStation loadingstation;

    public LoadingPort() {
    }

    public LoadingPort(int port) {
        this.port = port;
        session = null;
        reservation = null;
        occupied = false;
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

    public LoadingStation getLoadingstation() {
        return loadingstation;
    }

    public void setLoadingstation(LoadingStation loadingstation) {
        this.loadingstation = loadingstation;
    }

}
