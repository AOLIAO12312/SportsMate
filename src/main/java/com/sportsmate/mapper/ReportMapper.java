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
public interface ReportMapper {
    // Report相关方法
    @Select("SELECT * FROM reports")
    List<Report> getAllReports();

    @Select("SELECT * FROM reports WHERE id = #{id}")
    Report getReportById(Integer id);

    @Update("UPDATE reports SET status = #{status} WHERE id = #{id}")
    void updateReportStatus(@Param("id") Integer id, @Param("status") HandleStatus status);


    @Select("<script>" +
            "SELECT * FROM comment " +
            "<where>" +
            "  user_id IN (" +
            "    SELECT id FROM users " +
            "    <where>" +
            "      <if test='username != null'>username = #{username}</if> " +
            "      <if test='userType != null'>AND user_type = #{userType}</if> " +
            "      <if test='userStatus != null'>AND status = #{userStatus}</if> " +
            "    </where>" +
            "  )" +
            "</where>" +
            "</script>")
    List<Report> getReportsByFilters(String username, UserType userType, UserStatus userStatus);



}