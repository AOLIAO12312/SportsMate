package com.sportsmate.service.impl;

import com.sportsmate.mapper.AvailableTimeMapper;
import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.Weekday;
import com.sportsmate.service.AvailableTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class AvailableTimeServiceImpl implements AvailableTimeService {

    @Autowired
    private AvailableTimeMapper availableTimeMapper;

    @Override
    public void addAvailableTime(Integer loginUserId, Weekday weekday, LocalTime startTime, LocalTime endTime) {
        availableTimeMapper.addAvailableTime(loginUserId,weekday,startTime,endTime);
    }

    @Override
    public List<AvailableTime> findByUserId(Integer loginUserId) {
        return availableTimeMapper.findByUserId(loginUserId);
    }

    @Override
    public AvailableTime findById(Integer availableTimeId) {
        return availableTimeMapper.findById(availableTimeId);
    }
}
