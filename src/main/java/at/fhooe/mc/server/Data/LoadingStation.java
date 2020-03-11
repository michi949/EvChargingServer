package at.fhooe.mc.server.Data;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "loadingStation")
public class LoadingStation implements Serializable {
    @Id
    int stationNr;
    String owner;

    @OneToMany(mappedBy = "loadingStation", cascade = CascadeType.ALL)
    Set<LoadingPort> loadingPort;

    public LoadingStation() {
        stationNr = 0;
        owner = "undefiend";
    }

    /**
     * Sets parameter.
     * Revers sets the loadingstation on loading port.
     * @param stationNr
     * @param owner
     * @param loadingPort
     */
    public LoadingStation(int stationNr, String owner, List<LoadingPort> loadingPort) {
        this.stationNr = stationNr;
        this.owner = owner;
        this.loadingPort =  new HashSet<LoadingPort>(loadingPort);
        this.loadingPort.forEach(x -> x.setLoadingStation(this));
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

    public Set<LoadingPort> getLoadingPort() {
        return loadingPort;
    }

    public void setLoadingPort(Set<LoadingPort> loadingport) {
        this.loadingPort = loadingport;
    }

    @Override
    public String toString() {
        return "LoadingStation{" +
                "stationNr=" + stationNr +
                ", owner='" + owner + '\'' +
                ", loadingport=" + loadingPort +
                '}';
    }
}
