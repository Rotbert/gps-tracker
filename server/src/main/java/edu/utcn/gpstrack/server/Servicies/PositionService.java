package edu.utcn.gpstrack.server.Servicies;

import edu.utcn.gpstrack.server.DTOs.PositionDTO;
import edu.utcn.gpstrack.server.Entities.Position;
import edu.utcn.gpstrack.server.Repositories.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository){
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

        List<Position> positionList = (List<Position>) positionRepository.findAll();
        List<PositionDTO> positionDTOs = new ArrayList<>();

        for (Position position : positionList) {

            PositionDTO positionDTO = new PositionDTO();

            positionDTO.setTerminalId(position.getTerminalId());
            positionDTO.setLatitude(position.getLatitude());
            positionDTO.setLongitude(position.getLongitude());
            positionDTO.setId(position.getId());
            positionDTO.setCreationDate(position.getCreationDate());

            positionDTOs.add(positionDTO);
        }

        return positionDTOs;
    }

    public PositionDTO updatePosition(PositionDTO positionDTO) {

        Position oldPosition = positionRepository.findById(positionDTO.getId()).get();

        oldPosition.setTerminalId(positionDTO.getTerminalId());
        oldPosition.setLatitude(positionDTO.getLatitude());
        oldPosition.setLongitude(positionDTO.getLongitude());

        positionRepository.save(oldPosition);

        return getPositionDTO(oldPosition);
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

    public void deletePosition(Integer id) {

        Position position = positionRepository.findById(id).get();

        positionRepository.delete(position);

    }
}
