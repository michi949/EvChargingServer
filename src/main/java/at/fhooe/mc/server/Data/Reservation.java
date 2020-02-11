package at.fhooe.mc.server.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue
    int id;
    @Temporal(TemporalType.TIMESTAMP)
    Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    Date creationDate;

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStartDateAsStringForHtml(){
        return convertDateToString(this.startDate);
    }

    public String getEndDateAsStringForHtml(){
        return convertDateToString(this.endDate);
    }

    public String getCreationDateAsStringForHtml(){
        return convertDateToString(this.creationDate);
    }

    private String convertDateToString(Date date){
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(date);
    }
}
