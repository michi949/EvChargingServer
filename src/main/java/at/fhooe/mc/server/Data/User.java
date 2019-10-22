package at.fhooe.mc.server.Data;

import java.io.Serializable;

public class User implements Serializable {
    int id;
    String name;
    String email;
    int card;

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
}
