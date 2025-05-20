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

    @Select("SELECT * FROM reports WHERE reporter_id IN (SELECT id FROM users WHERE username = #{username})")
    List<Report> getReportsByUserId(Integer userId);

    @Select("SELECT * FROM reports WHERE reporter_id IN (SELECT id FROM users WHERE user_type = #{userType})")
    List<Report> getReportsByUserType(UserType userType);

    @Select("SELECT * FROM reports WHERE reporter_id IN (SELECT id FROM users WHERE status = #{userStatus})")
    List<Report> getReportsByUserStatus(UserStatus userStatus);


}