package edu.utcn.gpstrack.server.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.utcn.gpstrack.server.Entities.Position;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

    @Query(value = "SELECT * FROM position WHERE terminal_id = :terminalId AND creation_date >= :startDateParam AND creation_date <= :endDateParam", nativeQuery = true)
    List<Position> getPositionsBetween(@Param("terminalId") String terminalId,@Param("startDateParam") Date startDateParam, @Param("endDateParam") Date endDateParam);

}
