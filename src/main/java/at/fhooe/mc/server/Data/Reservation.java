package at.fhooe.mc.server.Data;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    int id;
    Date startDate;
    Date endDate;
    int port;
    User user;

    public Reservation() {
        id = 0;
        startDate = new Date();
        endDate = new Date();
        port = 0;
        user = new User();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
