package edu.utcn.gpstrack.server.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class PositionDTO {

    private Integer id;
    private String terminalId;
    private String latitude;
    private String longitude;
    private Date creationDate;
}
