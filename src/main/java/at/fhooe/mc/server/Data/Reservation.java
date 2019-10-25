package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue
    int id;
    Date startDate;
    Date endDate;

    @ManyToOne
    @JoinColumn
    User user;

    @ManyToOne
    @JoinColumn
    LoadingPort loadingport;

    public Reservation() {
        id = 0;
        startDate = new Date();
        endDate = new Date();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoadingPort getLoadingport() {
        return loadingport;
    }

    public void setLoadingport(LoadingPort loadingport) {
        this.loadingport = loadingport;
    }
}
