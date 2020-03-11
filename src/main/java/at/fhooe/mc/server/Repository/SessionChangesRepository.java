package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Reservation;
import at.fhooe.mc.server.Data.SessionChange;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessionChangesRepository extends CrudRepository<SessionChange, Integer> {
    @Query("SELECT u FROM SessionChange u WHERE u.id = ?1")
    SessionChange findSessionChangesById(int id);
}