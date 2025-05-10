package com.sportsmate.mapper;

import com.sportsmate.pojo.CoachProfile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CoachProfileMapper {

    @Select("select * from coach_profiles where user_id=#{id}")
    CoachProfile findByUserId(Integer id);

    @Update("update coach_profiles set real_name=#{realName},profile_description=#{profileDescription} where user_id=#{id}")
    void updateProfile(CoachProfile profile, Integer id);

    @Insert("insert into coach_profiles(user_id) values(#{registerUserId})")
    void add(Integer registerUserId);
}
