package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "car")
public class Car implements Serializable {
    @Id
    @GeneratedValue
    int id;
    String plate;
    String type;
    int count;
    Double capacity;
    boolean isOnePhase;
    int defaultPercent;
    boolean defaultIsSlowMode;

    @ManyToOne
    @JoinColumn
    User user;

    @OneToOne(mappedBy = "car")
    Session session;

    public Car() {}

    public Car(String plate, String type, Double capacity, boolean isOnePhase, int defaultPercent, boolean defaultIsSlowMode, User user) {
        this.plate = plate;
        this.type = type;
        this.capacity = capacity;
        this.isOnePhase = isOnePhase;
        this.defaultPercent = defaultPercent;
        this.defaultIsSlowMode = defaultIsSlowMode;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isOnePhase() {
        return isOnePhase;
    }

    public void setOnePhase(boolean onePhase) {
        isOnePhase = onePhase;
    }
}
