package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Reservation connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

}
