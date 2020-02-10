package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sessionchanges")
public class SessionChanges {
    @Id
    @GeneratedValue
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    double capacity;
    double minChargingPower;
    double optimizedPower;

    @ManyToOne
    @JoinColumn
    Session session;

    public SessionChanges() {
    }

    public SessionChanges(Session session){
        if(session.getCurrentCapacity() != null){
            this.capacity = session.getCurrentCapacity();
        } else {
            this.capacity = 0;
        }

        if(session.getMinPower() != null){
            this.minChargingPower = session.getMinPower();
        } else {
            this.minChargingPower = 0;
        }

        if(session.getOptimizedPower() != null){
            this.optimizedPower = session.getOptimizedPower();
        } else {
            this.optimizedPower = 0;
        }

        this.timestamp = new Date();
        this.session = session;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getMinChargingPower() {
        return minChargingPower;
    }

    public void setMinChargingPower(double minChargingPower) {
        this.minChargingPower = minChargingPower;
    }

    public double getOptimizedPower() {
        return optimizedPower;
    }

    public void setOptimizedPower(double optimizedPower) {
        this.optimizedPower = optimizedPower;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
