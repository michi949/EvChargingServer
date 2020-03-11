package at.fhooe.mc.server.Repository;

import at.fhooe.mc.server.Data.Session;
import at.fhooe.mc.server.Data.SolarPanels;
import at.fhooe.mc.server.Data.SystemReport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Date;
import java.util.Set;

@Repository
public interface SystemReportRepository extends CrudRepository<SystemReport, Integer> {

    @Query("SELECT u FROM SystemReport u WHERE u.timestamp > ?1")
    Set<SystemReport> findAllReportsInAllRange(Date startTime);

}