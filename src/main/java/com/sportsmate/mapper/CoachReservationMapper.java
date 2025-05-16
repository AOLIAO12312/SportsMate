package com.sportsmate.mapper;

import com.sportsmate.pojo.CoachReservation;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CoachReservationMapper {
    @Select("select * from coach_reservation where coach_id=#{coachUserId} AND start_time=#{targetStartDateTime}")
    CoachReservation findByCoachIdAndDateTime(Integer coachUserId, LocalDateTime targetStartDateTime);

    @Insert("INSERT INTO coach_reservation (user_id, coach_id, start_time, end_time, status) " +
            "VALUES (#{userId}, #{coachId}, #{startTime}, #{endTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addReservation(CoachReservation coachReservation);

    @Select("select * from coach_reservation where user_id=#{loginUserId}")
    List<CoachReservation> findByUserId(Integer loginUserId);

    @Select("select * from coach_reservation where coach_id=#{loginUserId}")
    List<CoachReservation> findByCoachId(Integer loginUserId);

    @Update("update coach_reservation set status=#{status}")
    void setStatus(CoachReservation coachReservation);

    @Select("select * from coach_reservation where id=#{reservationId}")
    CoachReservation findById(Integer reservationId);
}
