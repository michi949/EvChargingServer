package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Car;
import at.fhooe.mc.server.Data.LoadingStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Car connection to Database.
 * Possibility to add own methodes for the database.
 */
@Repository
public interface LoadingStationRepository extends CrudRepository<LoadingStation, Integer> {

}
