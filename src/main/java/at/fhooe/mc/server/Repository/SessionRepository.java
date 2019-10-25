package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.Weather;
import org.springframework.data.repository.CrudRepository;

/**
 * Session connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface SessionRepository extends CrudRepository<Session, Integer> {

}
