package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

/**
 * Reservation connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Query("SELECT u FROM Reservation u WHERE u.id = ?1")
    Reservation findReservationById(int id);

    @Query("SELECT u FROM Reservation u WHERE u.startDate > ?1 AND u.endDate < ?2")
    Set<Reservation> findReservationByDateRange(Date beginDate, Date endDate);
}
