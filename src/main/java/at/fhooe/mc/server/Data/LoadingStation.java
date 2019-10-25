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

    public int getStationNr() {
        return stationNr;
    }

    public void setStationNr(int stationNr) {
        this.stationNr = stationNr;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<LoadingPort> getPorts() {
        return ports;
    }

    public void setPorts(Set<LoadingPort> ports) {
        this.ports = ports;
    }
}
