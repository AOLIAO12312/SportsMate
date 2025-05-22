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

    @Update("UPDATE reports SET status = #{status}, reply_message = #{replyMessage} WHERE id = #{id}")
    void updateReportStatus(@Param("id") Integer id, @Param("status") HandleStatus status, @Param("replyMessage") String replyMessage);


    // ReportMapper
    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername})")
    List<Report> getReportsByReportername(String reportername);

    @Select("SELECT * FROM reports WHERE reported_id = (SELECT id FROM users WHERE username = #{reportedname})")
    List<Report> getReportsByReportedname(String reportedname);

    @Select("SELECT * FROM reports WHERE status = #{status}")
    List<Report> getReportsByStatus(HandleStatus status);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND reported_id = (SELECT id FROM users WHERE username = #{reportedname})")
    List<Report> getReportsByReporternameAndReportedname(String reportername, String reportedname);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND status = #{status}")
    List<Report> getReportsByReporternameAndStatus(String reportername, HandleStatus status);

    @Select("SELECT * FROM reports WHERE reported_id = (SELECT id FROM users WHERE username = #{reportedname}) AND status = #{status}")
    List<Report> getReportsByReportednameAndStatus(String reportedname, HandleStatus status);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND reported_id = (SELECT id FROM users WHERE username = #{reportedname}) AND status = #{status}")
    List<Report> getReportsByReporternameReportednameAndStatus(String reportername, String reportedname, HandleStatus status);
}



