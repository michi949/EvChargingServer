package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

/**
 * Session connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

    @Query("SELECT u FROM Session u WHERE u.id = ?1")
    Session findSessionById(int id);

    @Query("SELECT u FROM Session u WHERE u.startDate < ?1 AND u.endDate > ?1")
    Set<Session> findAllSessionsCurrentRunning(Date currentDate);

    @Query("SELECT u FROM Session u WHERE u.startDate > ?1 AND u.endDate < ?1")
    Set<Session> findAllSessionNotRunning(Date currentDate);
}
