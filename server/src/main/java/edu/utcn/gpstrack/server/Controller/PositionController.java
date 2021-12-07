package edu.utcn.gpstrack.server.Controller;

import edu.utcn.gpstrack.server.DTOs.PositionDTO;
import edu.utcn.gpstrack.server.Servicies.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/save")
    public PositionDTO savePosition(@RequestBody PositionDTO positionDTO) {
        return positionService.savePosition(positionDTO);
    }

    @GetMapping("/get-all")
    public List<PositionDTO> getAllPositions() {
        return positionService.getAllPositions();
    }

    @GetMapping("/get-between/{startDate}/{endDate}")
    public List<PositionDTO> getPositionsBetween(@PathVariable String startDate, @PathVariable String endDate) throws ParseException {
        return positionService.getPositionsBetween(new SimpleDateFormat("yyyy-MM-dd").parse(startDate), new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
    }

    @PutMapping("/update")
    public PositionDTO updatePosition(@RequestBody PositionDTO positionDTO) {
        return positionService.updatePosition(positionDTO);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePosition(@PathVariable Integer id) {
        return positionService.deletePosition(id);
    }
}
