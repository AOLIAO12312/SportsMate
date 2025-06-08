package com.sportsmate.mapper;

import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationStatus;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CoachReservationMapper {
    @Select("select * from coach_reservation where coach_id=#{coachUserId} AND start_time=#{targetStartDateTime} AND status != #{status}")
    CoachReservation findByCoachIdAndDateTime(Integer coachUserId, LocalDateTime targetStartDateTime,String status);

    @Insert("INSERT INTO coach_reservation (user_id, coach_id, start_time, end_time, status,venue_id) " +
            "VALUES (#{userId}, #{coachId}, #{startTime}, #{endTime}, #{status},#{venueId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void addReservation(CoachReservation coachReservation);

    @Select("select * from coach_reservation where user_id=#{loginUserId}")
    List<CoachReservation> findByUserId(Integer loginUserId);

    @Select("select * from coach_reservation where coach_id=#{loginUserId}")
    List<CoachReservation> findByCoachId(Integer loginUserId);

    @Update("update coach_reservation set status=#{status} where id=#{id}")
    void setStatus(CoachReservation coachReservation);

    @Select("select * from coach_reservation where id=#{reservationId}")
    CoachReservation findById(Integer reservationId);

    @Select("select * from coach_reservation where user_id=#{loginUserId} AND status=#{reservationStatus}")
    List<CoachReservation> listByUserIdAndStatus(Integer loginUserId, ReservationStatus reservationStatus);

    @Select("select * from coach_reservation where coach_id=#{loginUserId} AND status=#{reservationStatus}")
    List<CoachReservation> listByCoachIdAndStatus(Integer loginUserId, ReservationStatus reservationStatus);
}
