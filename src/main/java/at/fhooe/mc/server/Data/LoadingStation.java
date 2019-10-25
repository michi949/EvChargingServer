package at.fhooe.mc.server.Data;


import javax.persistence.*;
import java.util.Set;

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
}
