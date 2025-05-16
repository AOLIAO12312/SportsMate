package com.sportsmate.mapper;

import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.Weekday;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalTime;
import java.util.List;

@Mapper
public interface AvailableTimeMapper {
    @Insert("INSERT INTO available_time (coach_id, weekday, start_time, end_time) " +
            "VALUES (#{loginUserId}, #{weekday}, #{startTime}, #{endTime})")
    void addAvailableTime(@Param("loginUserId") Integer loginUserId,
                          @Param("weekday") Weekday weekday,
                          @Param("startTime") LocalTime startTime,
                          @Param("endTime") LocalTime endTime);

    @Select("select * from available_time where coach_id=#{loginUserId}")
    List<AvailableTime> findByUserId(Integer loginUserId);

    @Select("select * from available_time where id=#{availableTimeId}")
    AvailableTime findById(Integer availableTimeId);
}
