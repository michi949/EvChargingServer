package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue
    int id;
    String name;
    String email;
    int card;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Car> car;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Reservation> reservation;

    public User() {
        id = 0;
        name = "Michael";
        email = "reder949@gmail.com";
        card = 2131231412;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public Set<Car> getCar() {
        return car;
    }

    public void setCar(Set<Car> car) {
        this.car = car;
    }

    public Set<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(Set<Reservation> reservation) {
        this.reservation = reservation;
    }
}
