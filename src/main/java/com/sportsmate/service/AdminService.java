package com.sportsmate.service;

import com.sportsmate.pojo.*;

import java.time.LocalDate;
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
    PageBean<UserWithCoachInfo> getFilteredUsers(Integer pageNum, Integer pageSize, String username, UserType userType, UserStatus userStatus, Integer userId);

    // 筛选matchcomment 接口
    PageBean<MatchComment> getMatchCommentsByUsername1(Integer pageNum, Integer pageSize, String username1);
    PageBean<MatchComment> getMatchCommentsByMatchId(Integer pageNum, Integer pageSize, Integer match_id);
    PageBean<MatchComment> getMatchCommentsByUsername1AndUsername2(Integer pageNum, Integer pageSize, String username1, String username2);
    PageBean<MatchComment> getAllMatchComments(Integer pageNum, Integer pageSize);
    PageBean<MatchComment> getMatchCommentsByUsername1AndMatchId(Integer pageNum, Integer pageSize, String username1, Integer matchId);

    // 筛选reservationComment 接口
    PageBean<ReservationComment> getReservationCommentsByUsername(Integer pageNum, Integer pageSize, String username);
    PageBean<ReservationComment> getReservationCommentsByCoachname(Integer pageNum, Integer pageSize, String coachname);
    PageBean<ReservationComment> getReservationCommentsByUsernameAndCoachname(Integer pageNum, Integer pageSize, String username, String coachname);
    PageBean<ReservationComment> getAllReservationComments(Integer pageNum, Integer pageSize);

    // 筛选report 接口
    PageBean<Report> getReportsByReportername(Integer pageNum, Integer pageSize, String reportername);
    PageBean<Report> getReportsByReportedname(Integer pageNum, Integer pageSize, String reportedname);
    PageBean<Report> getReportsByStatus(Integer pageNum, Integer pageSize, HandleStatus status);
    PageBean<Report> getReportsByReporternameAndReportedname(Integer pageNum, Integer pageSize, String reportername, String reportedname);
    PageBean<Report> getReportsByReporternameAndStatus(Integer pageNum, Integer pageSize, String reportername, HandleStatus status);
    PageBean<Report> getReportsByReportednameAndStatus(Integer pageNum, Integer pageSize, String reportedname, HandleStatus status);
    PageBean<Report> getReportsByReporternameReportednameAndStatus(Integer pageNum, Integer pageSize, String reportername, String reportedname, HandleStatus status);
    PageBean<Report> getAllReports(Integer pageNum, Integer pageSize);

    // 筛选申述 接口
    PageBean<Appeal> getAppealsByAppellantname(Integer pageNum, Integer pageSize, String appellantname);
    PageBean<Appeal> getAppealsByStatus(Integer pageNum, Integer pageSize, HandleStatus status);
    PageBean<Appeal> getAppealsByAppellantnameAndStatus(Integer pageNum, Integer pageSize, String appellantname, HandleStatus status);
    PageBean<Appeal> getAllAppeals(Integer pageNum, Integer pageSize);
    // 警告用户
    void warnUser(Integer userId);

    int getCountByDate(LocalDate date);

    int getReportCountByDate(LocalDate date);

    // 按照评论ID筛选比赛评论
    PageBean<MatchComment> getMatchCommentsById(Integer pageNum, Integer pageSize, Integer commentId);

    // 按照评论ID筛选预约评论
    PageBean<ReservationComment> getReservationCommentsById(Integer pageNum, Integer pageSize, Integer commentId);
}