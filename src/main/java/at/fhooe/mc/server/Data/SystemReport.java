package at.fhooe.mc.server.Data;

import at.fhooe.mc.server.Repository.SessionChangesRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "systemReport")
public class SystemReport implements Serializable {
    @Id
    @GeneratedValue(
            strategy= GenerationType.IDENTITY
    )
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    @OneToMany(mappedBy = "systemReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<SessionChange> sessionChanges = new HashSet<>();

    double solarPower;
    double neededPower;

    public SystemReport() {
    }

    public SystemReport(ArrayList<Session> sessions, double solarPower) {
        this.solarPower = solarPower;
        this.timestamp = new Date();
        generateSessionChanges(sessions);
    }

    private void generateSessionChanges(ArrayList<Session> sessions){
        for(Session session : sessions){
            SessionChange changes = new SessionChange(session, this);
            this.sessionChanges.add(changes);
            neededPower += session.getChargingPower();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getSolarPower() {
        return solarPower;
    }

    public void setSolarPower(double solarPower) {
        this.solarPower = solarPower;
    }

    public double getNeededPower() {
        return neededPower;
    }

    public void setNeededPower(double neededPower) {
        this.neededPower = neededPower;
    }

    public Set<SessionChange> getSessionChanges() {
        return sessionChanges;
    }

    public void setSessionChanges(Set<SessionChange> sessionChanges) {
        this.sessionChanges = sessionChanges;
    }
}
