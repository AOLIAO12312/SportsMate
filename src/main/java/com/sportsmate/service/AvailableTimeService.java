package com.sportsmate.service;

import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.Weekday;

import java.time.LocalTime;
import java.util.List;

public interface AvailableTimeService {

    void addAvailableTime(Integer loginUserId, Weekday weekday, LocalTime startTime, LocalTime endTime);

    List<AvailableTime> findByUserId(Integer loginUserId);

    AvailableTime findById(Integer availableTimeId);

    void deleteAvailableTime(Integer availableTimeId);
}
