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


    @Select("<script>" +
            "SELECT * FROM appeals " +
            "<where>" +
            "  id IN (" +
            "    SELECT id FROM users " +
            "    <where>" +
            "      <if test='username != null'>username = #{username}</if> " +
            "      <if test='userType != null'>AND user_type = #{userType}</if> " +
            "      <if test='userStatus != null'>AND status = #{userStatus}</if> " +
            "    </where>" +
            "  )" +
            "</where>" +
            "</script>")
    List<Appeal> getAppealsByFilters(String username, UserType userType, UserStatus userStatus);


}