package com.sportsmate.dto;

import lombok.Data;

@Data
public class CoachProfileDTO {
    private Integer userId;
    private String realName;
    private String profileDescription;
    private String coachedSportName;
    private Float rating;
    private Boolean isActive;
}

