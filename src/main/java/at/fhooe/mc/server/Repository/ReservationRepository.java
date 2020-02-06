package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Reservation connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Query("SELECT u FROM Reservation u WHERE u.id = ?1")
    Reservation findReservationById(int id);

}
