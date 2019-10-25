package at.fhooe.mc.server.Data;


import java.util.Set;

public class LoadingStation {
    int stationNr;
    String owner;
    Set<LoadingPort> ports;

    public LoadingStation() {
        stationNr = 0;
        owner = "undefiend";
    }
}
