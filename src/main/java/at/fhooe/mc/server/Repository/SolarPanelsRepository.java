package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.SolarPanels;
import at.fhooe.mc.server.Data.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SolarPanelsRepository extends CrudRepository<SolarPanels, Integer> {

}