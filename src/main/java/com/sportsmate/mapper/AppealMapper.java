package com.sportsmate.mapper;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppealMapper {

    // Appeal相关方法
    @Select("SELECT * FROM appeals")
    List<Appeal> getAllAppeals();

    @Select("SELECT * FROM appeals WHERE id = #{id}")
    Appeal getAppealById(Integer id);

    @Update("UPDATE appeals SET status = #{status} WHERE id = #{id}")
    void updateAppealStatus(@Param("id") Integer id, @Param("status") HandleStatus status);

    @Select("SELECT * FROM appeals WHERE appellant_id IN (SELECT id FROM users WHERE username = #{username})")
    List<Appeal> getAppealsByUserId(Integer userId);

    @Select("SELECT * FROM appeals WHERE appellant_id IN (SELECT id FROM users WHERE user_type = #{userType})")
    List<Appeal> getAppealsByUserType(UserType userType);

    @Select("SELECT * FROM appeals WHERE appellant_id IN (SELECT id FROM users WHERE status = #{userStatus})")
    List<Appeal> getAppealsByUserStatus(UserStatus userStatus);


}