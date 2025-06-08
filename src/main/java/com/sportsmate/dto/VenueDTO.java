package com.sportsmate.dto;

import lombok.Data;

@Data
public class VenueDTO {
    private Integer id;
    private String name;
    private String openingTime;
    private String closingTime;
    private String fullAddress;
    private Double rating;
}

