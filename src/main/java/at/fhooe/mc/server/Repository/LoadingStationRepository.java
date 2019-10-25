package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import org.springframework.data.repository.CrudRepository;

/**
 * Car connection to Database.
 * Possibility to add own methodes for the database.
 */
public interface LoadingStationRepository extends CrudRepository<Car, Integer> {

}
