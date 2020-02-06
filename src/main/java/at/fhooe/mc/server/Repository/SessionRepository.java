package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Session connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {

    @Query("SELECT u FROM Session u WHERE u.id = ?1")
    Session findSessionById(int id);
}
