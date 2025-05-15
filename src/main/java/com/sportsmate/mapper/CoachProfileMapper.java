package com.sportsmate.mapper;

import com.sportsmate.pojo.CoachProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CoachProfileMapper {

    @Select("select * from coach_profiles where user_id=#{id}")
    CoachProfile findByUserId(Integer id);

    @Update("update coach_profiles set real_name=#{realName},profile_description=#{profileDescription},coached_sports=#{coachedSports} where user_id=#{userId}")
    void updateProfile(CoachProfile profile);

    @Insert("insert into coach_profiles(user_id) values(#{registerUserId})")
    void add(Integer registerUserId);

    @Select("select * from coach_profiles where sport_id=#{sportId}")
    List<CoachProfile> list(Integer sportId);
}
