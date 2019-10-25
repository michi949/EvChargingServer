package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "loadingport")
public class LoadingPort {
    @Id
    int port;
    boolean occupied;

    @OneToOne(mappedBy = "loadingport")
    Session session;

    @OneToMany(mappedBy = "loadingport", cascade = CascadeType.ALL)
    Set<Reservation> reservation;

    @ManyToOne
    @JoinColumn
    LoadingStation loadingstation;

    public LoadingPort() {

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

    public Set<Reservation> getReservations() {
        return reservation;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservation = reservations;
    }
}
