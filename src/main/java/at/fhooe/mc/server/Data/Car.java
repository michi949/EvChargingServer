package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Car")
public class Car implements Serializable {
    @Id
    @GeneratedValue
    int id;
    String plate;
    int count;
    Double capacity;
    Double maxPower;
    User owner;

    public Car() {
        id = 0;
        plate = "GM-WL456";
        count = 0;
        capacity = 56.0;
        maxPower = 100.0;
        owner = new User();
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

    public Double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }
}
