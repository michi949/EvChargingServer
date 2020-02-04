package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingPort;
import at.fhooe.mc.server.Data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Car connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface LoadingPortRepository extends CrudRepository<LoadingPort, Integer> {

    @Query("SELECT u FROM LoadingPort u WHERE u.port = ?1")
    LoadingPort findLoadingPortById(Integer port);

}
