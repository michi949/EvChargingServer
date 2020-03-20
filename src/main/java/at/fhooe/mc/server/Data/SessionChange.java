package at.fhooe.mc.server.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sessionChange")
public class SessionChange implements Serializable {
    @Id
    @GeneratedValue(
            strategy= GenerationType.IDENTITY
    )
    int id;

    @Temporal(TemporalType.TIMESTAMP)
    Date timestamp;

    int portId; //Only for Frontenduse
    double capacity;
    double minChargingPower;
    double optimizedPower;
    double chargingPower;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    Session session;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    SystemReport systemReport;

    public SessionChange() {
    }

    public SessionChange(Session session, SystemReport systemReport){
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

        if(session.getChargingPower() != null){
            this.chargingPower = session.getChargingPower();
        } else {
            this.chargingPower = 0;
        }

        if(session.getLoadingPort() != null){
            this.portId = session.getLoadingPort().port;
        } else {
            this.portId = 0;
        }

        this.systemReport = systemReport;
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

    public double getChargingPower() {
        return chargingPower;
    }

    public void setChargingPower(double chargingPower) {
        this.chargingPower = chargingPower;
    }

    public SystemReport getSystemReport() {
        return systemReport;
    }

    public void setSystemReport(SystemReport systemReport) {
        this.systemReport = systemReport;
    }

    public int getPortId() {
        return portId;
    }

    public void setPortId(int portId) {
        this.portId = portId;
    }
}
