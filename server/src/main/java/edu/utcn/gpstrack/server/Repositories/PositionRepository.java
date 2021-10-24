package edu.utcn.gpstrack.server.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.utcn.gpstrack.server.Entities.Position;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
}
