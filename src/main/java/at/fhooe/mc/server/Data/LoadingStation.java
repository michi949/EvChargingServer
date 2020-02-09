package at.fhooe.mc.server.Data;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "loadingstation")
public class LoadingStation {
    @Id
    int stationNr;
    String owner;

    @OneToMany(mappedBy = "loadingstation", cascade = CascadeType.ALL)
    Set<LoadingPort> loadingport;

    public LoadingStation() {
        stationNr = 0;
        owner = "undefiend";
    }

    /**
     * Sets parameter.
     * Revers sets the loadingstation on loading port.
     * @param stationNr
     * @param owner
     * @param loadingport
     */
    public LoadingStation(int stationNr, String owner, List<LoadingPort> loadingport) {
        this.stationNr = stationNr;
        this.owner = owner;
        this.loadingport =  new HashSet<LoadingPort>(loadingport);
        this.loadingport.forEach(x -> x.setLoadingstation(this));
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

    public Set<LoadingPort> getLoadingport() {
        return loadingport;
    }

    public void setLoadingport(Set<LoadingPort> loadingport) {
        this.loadingport = loadingport;
    }

    @Override
    public String toString() {
        return "LoadingStation{" +
                "stationNr=" + stationNr +
                ", owner='" + owner + '\'' +
                ", loadingport=" + loadingport +
                '}';
    }
}
