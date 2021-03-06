package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * User connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.card = ?1")
    User findUserByCard(int card);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findUserById(int id);

}
