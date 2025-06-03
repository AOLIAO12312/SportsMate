package com.sportsmate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sportsmate.pojo.Weekday;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AvailableTimeDTO {
    private Integer id;
    private Weekday weekday;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
}
