package com.sportsmate.service.impl;

import com.sportsmate.mapper.CoachReservationMapper;
import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.service.CoachReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.sportsmate.utils.DateUtil.getNextDateTime;

@Service
public class CoachReservationServiceImpl implements CoachReservationService {

    @Autowired
    private CoachReservationMapper coachReservationMapper;

    @Override
    public void requestReservation(Integer loginUserId, AvailableTime availableTime) {
        LocalDateTime targetStartDateTime = getNextDateTime(availableTime.getWeekday(), availableTime.getStartTime());
        LocalDateTime targetEndDateTime = getNextDateTime(availableTime.getWeekday(),availableTime.getEndTime());
// 验证距离当前时间是否大于等于1小时
        Duration duration = Duration.between(LocalDateTime.now(), targetStartDateTime);

        if (duration.toMinutes() < 60) {
            throw new RuntimeException("预约时间必须距离当前时间至少1小时");
        }
        if(targetStartDateTime.isAfter(targetEndDateTime)){
            throw new RuntimeException("当前时间段不可预约");
        }
        Integer coachUserId = availableTime.getCoachId();

        CoachReservation existingCoachReservation =  coachReservationMapper.findByCoachIdAndDateTime(coachUserId,targetStartDateTime);
        if(existingCoachReservation != null){
            //抛出错误
            throw new RuntimeException("该时间段已被预约，请选择其他时间");
        }
        CoachReservation coachReservation = new CoachReservation();
        coachReservation.setCoachId(coachUserId);
        coachReservation.setUserId(loginUserId);
        coachReservation.setStartTime(targetStartDateTime);
        coachReservation.setEndTime(targetEndDateTime);
        coachReservationMapper.addReservation(coachReservation);
    }

    @Override
    public List<CoachReservation> findByUserId(Integer loginUserId) {
        return coachReservationMapper.findByUserId(loginUserId);
    }

    @Override
    public Object findByCoachId(Integer loginUserId) {
        return coachReservationMapper.findByCoachId(loginUserId);
    }

    @Override
    public void setStatus(CoachReservation coachReservation) {
        coachReservationMapper.setStatus(coachReservation);
    }

    @Override
    public CoachReservation findById(Integer reservationId) {
        return coachReservationMapper.findById(reservationId);
    }
}
