package com.sportsmate.mapper;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReportMapper {
    // Report相关方法


    @Select("SELECT * FROM reports WHERE id = #{id}")
    Report getReportById(Integer id);

    @Update("UPDATE reports SET status = #{status}, reply_message = #{replyMessage} WHERE id = #{id}")
    void updateReportStatus(@Param("id") Integer id, @Param("status") HandleStatus status, @Param("replyMessage") String replyMessage);

    @Select("SELECT * FROM reports WHERE reporter_id = #{reporterId} AND ((comment_id = #{commentId} AND match_id IS NULL) OR (match_id = #{matchId} AND comment_id IS NULL))")
    Report getReportByReporterAndCommentOrMatch(Integer reporterId, Integer commentId, Integer matchId);

    @Update("UPDATE reports SET reason = #{reason}, status = #{status}, reply_message = #{replyMessage} WHERE id = #{id}")
    void updateReport(Report report);

    // ReportMapper
    @Select("SELECT * FROM reports ORDER BY created_at DESC")
    List<Report> getAllReports();

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) ORDER BY created_at DESC")
    List<Report> getReportsByReportername(String reportername);

    @Select("SELECT * FROM reports WHERE reported_id = (SELECT id FROM users WHERE username = #{reportedname}) ORDER BY created_at DESC")
    List<Report> getReportsByReportedname(String reportedname);

    @Select("SELECT * FROM reports WHERE status = #{status} ORDER BY created_at DESC")
    List<Report> getReportsByStatus(HandleStatus status);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND reported_id = (SELECT id FROM users WHERE username = #{reportedname}) ORDER BY created_at DESC")
    List<Report> getReportsByReporternameAndReportedname(String reportername, String reportedname);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND status = #{status} ORDER BY created_at DESC")
    List<Report> getReportsByReporternameAndStatus(String reportername, HandleStatus status);

    @Select("SELECT * FROM reports WHERE reported_id = (SELECT id FROM users WHERE username = #{reportedname}) AND status = #{status} ORDER BY created_at DESC")
    List<Report> getReportsByReportednameAndStatus(String reportedname, HandleStatus status);

    @Select("SELECT * FROM reports WHERE reporter_id = (SELECT id FROM users WHERE username = #{reportername}) AND reported_id = (SELECT id FROM users WHERE username = #{reportedname}) AND status = #{status} ORDER BY created_at DESC")
    List<Report> getReportsByReporternameReportednameAndStatus(String reportername, String reportedname, HandleStatus status);

    @Select("SELECT COUNT(*) FROM reports WHERE DATE(created_at) = #{date}")
    int getReportCountByDate(LocalDate date);
}



