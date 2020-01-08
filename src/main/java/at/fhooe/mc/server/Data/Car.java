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
    int count;
    Double capacity;
    boolean isOnePhase;

    @ManyToOne
    @JoinColumn
    User user;

    @OneToOne(mappedBy = "car")
    Session session;

    public Car() {
        id = 0;
        plate = "GM-WL456";
        count = 0;
        capacity = 56.0;
        isOnePhase = false;
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
