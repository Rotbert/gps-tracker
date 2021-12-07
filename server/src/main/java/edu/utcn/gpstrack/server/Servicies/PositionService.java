package edu.utcn.gpstrack.server.Servicies;

import edu.utcn.gpstrack.server.DTOs.PositionDTO;
import edu.utcn.gpstrack.server.Entities.Position;
import edu.utcn.gpstrack.server.Repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PositionDTO savePosition(PositionDTO positionDTO) {

        Position position = new Position();

        position.setTerminalId(positionDTO.getTerminalId());
        position.setLatitude(positionDTO.getLatitude());
        position.setLongitude(positionDTO.getLongitude());
        position.setCreationDate(positionDTO.getCreationDate());

        Position savedPosition = positionRepository.save(position);

        return getPositionDTO(savedPosition);
    }

    public List<PositionDTO> getAllPositions() {

        List<Position> positionList = positionRepository.findAll();
        List<PositionDTO> positionDTOs = new ArrayList<>();

        for (Position position : positionList) {
            positionDTOs.add(getPositionDTO(position));
        }

        return positionDTOs;
    }

    public List<PositionDTO> getPositionsBetween(Date startDate, Date endDate) {

        List<Position> positionList =
                positionRepository.getPositionsBetween(startDate,endDate);
        List<PositionDTO> positionDTOs = new ArrayList<>();

        for (Position position : positionList) {
            positionDTOs.add(getPositionDTO(position));
        }

        return positionDTOs;
    }

    public PositionDTO updatePosition(PositionDTO positionDTO) {

        if (positionRepository.findById(positionDTO.getId()).isPresent()) {
            Position oldPosition = positionRepository.findById(positionDTO.getId()).get();

            oldPosition.setTerminalId(positionDTO.getTerminalId());
            oldPosition.setLatitude(positionDTO.getLatitude());
            oldPosition.setLongitude(positionDTO.getLongitude());

            positionRepository.save(oldPosition);

            return getPositionDTO(oldPosition);
        }
        return savePosition(positionDTO);
    }

    private PositionDTO getPositionDTO(Position position) {
        PositionDTO positionDTO = new PositionDTO();

        positionDTO.setTerminalId(position.getTerminalId());
        positionDTO.setLatitude(position.getLatitude());
        positionDTO.setLongitude(position.getLongitude());
        positionDTO.setId(position.getId());
        positionDTO.setCreationDate(position.getCreationDate());

        return positionDTO;
    }

    public String deletePosition(Integer id) {
        if (positionRepository.findById(id).isPresent()) {
            Position position = positionRepository.findById(id).get();

            positionRepository.delete(position);
            return "Deleted id " + id + ".";
        }
        return "Nothing to delete.";
    }
}
