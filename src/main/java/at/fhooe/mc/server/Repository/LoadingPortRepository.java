package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingPort;
import org.springframework.data.repository.CrudRepository;

/**
 * Car connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface LoadingPortRepository extends CrudRepository<LoadingPort, Integer> {

}
