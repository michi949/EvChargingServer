package at.fhooe.mc.server.Data;

import java.util.Set;

public class LoadingPort {
    int port;
    boolean occupied;
    Session session;
    Set<Reservation> reservations;

    public LoadingPort() {

    }
}
