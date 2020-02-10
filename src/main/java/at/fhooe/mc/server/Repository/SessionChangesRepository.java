package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Reservation;
import at.fhooe.mc.server.Data.SessionChanges;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SessionChangesRepository extends CrudRepository<SessionChanges, Integer> {
    @Query("SELECT u FROM SessionChanges u WHERE u.id = ?1")
    SessionChanges findSessionChangesById(int id);
}