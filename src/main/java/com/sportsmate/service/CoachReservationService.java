package com.sportsmate.service;

import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.CoachReservation;

import java.util.List;

public interface CoachReservationService {
    void requestReservation(Integer loginUserId, AvailableTime availableTime);

    List<CoachReservation> findByUserId(Integer loginUserId);

    Object findByCoachId(Integer loginUserId);

    void setStatus(CoachReservation coachReservation);

    CoachReservation findById(Integer reservationId);
}
