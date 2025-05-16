package com.sportsmate.dto;

import com.sportsmate.pojo.Weekday;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AvailableTimeDTO {
    private Integer id;
    private Weekday weekday;
    private LocalTime startTime;
    private LocalTime endTime;
}
