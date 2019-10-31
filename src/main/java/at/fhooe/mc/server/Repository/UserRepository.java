package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.User;
import at.fhooe.mc.server.Data.Weather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * User connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.card = ?1")
    User findUserByCard(Integer card);

}
