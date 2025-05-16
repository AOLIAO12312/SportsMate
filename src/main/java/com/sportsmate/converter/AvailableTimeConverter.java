package com.sportsmate.converter;

import com.sportsmate.dto.AvailableTimeDTO;
import com.sportsmate.pojo.AvailableTime;
import org.springframework.stereotype.Component;

@Component
public class AvailableTimeConverter {
    public AvailableTimeDTO toDTO(AvailableTime availableTime){
        AvailableTimeDTO dto = new AvailableTimeDTO();
        dto.setId(availableTime.getId());
        dto.setWeekday(availableTime.getWeekday());
        dto.setStartTime(availableTime.getStartTime());
        dto.setEndTime(availableTime.getEndTime());
        return dto;
    }
}
