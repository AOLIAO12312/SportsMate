package com.sportsmate.service;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.HandleStatus;
import java.util.List;

public interface AdminService {
    // 查看举报
    List<Report> getReports();

    // 查看申诉
    List<Appeal> getAppeals();

    // 管理员处理举报
    String handleReport(Integer reportId, String message);

    // 管理员处理申诉
    String handleAppeal(Integer appealId, String message);

    // 封禁用户
    void banUser(Integer userId);

    // 解封用户
    void unbanUser(Integer userId);

    // 查看单个举报
    Report getReportById(Integer reportId);

    // 查看单个申诉
    Appeal getAppealById(Integer appealId);

    // 筛选用户
    List<User> getFilteredUsers(String username, UserType userType, UserStatus userStatus);

    // 筛选matchcomment 接口
    List<MatchComment> getMatchCommentsByUsername1(String username1);
    List<MatchComment> getMatchCommentsByMatchId(Integer match_id);
    List<MatchComment> getMatchCommentsByUsername1AndUsername2(String username1, String username2);
    List<MatchComment> getAllMatchComments();

    // 筛选reservationComment 接口
    List<ReservationComment> getReservationCommentsByUsername(String username);
    List<ReservationComment> getReservationCommentsByCoachname(String coachname);
    List<ReservationComment> getReservationCommentsByUsernameAndCoachname(String username, String coachname);
    List<ReservationComment> getAllReservationComments();

    // 筛选report 接口
    List<Report> getReportsByReportername(String reportername);
    List<Report> getReportsByReportedname(String reportedname);
    List<Report> getReportsByStatus(HandleStatus status);
    List<Report> getReportsByReporternameAndReportedname(String reportername, String reportedname);
    List<Report> getReportsByReporternameAndStatus(String reportername, HandleStatus status);
    List<Report> getReportsByReportednameAndStatus(String reportedname, HandleStatus status);
    List<Report> getReportsByReporternameReportednameAndStatus(String reportername, String reportedname, HandleStatus status);
    List<Report> getAllReports();

    // 筛选申述 接口
    List<Appeal> getAppealsByAppellantname(String appellantname);
    List<Appeal> getAppealsByStatus(HandleStatus status);
    List<Appeal> getAppealsByAppellantnameAndStatus(String appellantname, HandleStatus status);
    List<Appeal> getAllAppeals();

    // 警告用户
    void warnUser(Integer userId);
}