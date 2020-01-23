package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Data.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Session connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface SessionRepository extends CrudRepository<Session, Integer> {

    /**
    @Query("SELECT u FROM Session u WHERE u.card = ?1")
    Session findUserByCard(int port); */

}
