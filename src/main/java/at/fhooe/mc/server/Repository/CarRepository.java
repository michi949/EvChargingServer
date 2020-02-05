package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Car connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface CarRepository extends CrudRepository<Car, Integer> {

}
