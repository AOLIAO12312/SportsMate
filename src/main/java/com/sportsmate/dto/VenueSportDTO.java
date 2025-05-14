package com.sportsmate.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VenueSportDTO {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer venueId;

    private String sportName;
    private Integer remainSpots;
}
