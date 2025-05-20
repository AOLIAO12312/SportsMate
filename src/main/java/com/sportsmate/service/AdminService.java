package com.sportsmate.service;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.Comment;
import java.util.List;

public interface AdminService {
    // 查看举报
    List<Report> getReports();

    // 查看申诉
    List<Appeal> getAppeals();

    // 管理员处理举报
    void handleReport(Integer reportId);

    // 管理员处理申诉
    void handleAppeal(Integer appealId);

    // 封禁用户
    void banUser(Integer userId);

    // 解封用户
    void unbanUser(Integer userId);

    // 查看单个举报
    Report getReportById(Integer reportId);

    // 查看单个申诉
    Appeal getAppealById(Integer appealId);

    //根据筛选举报，申述和评论
    List<Report> getReportsByUsername(String username);

    List<Report> getReportsByUserType(UserType userType);

    List<Report> getReportsByUserStatus(UserStatus userStatus);

    List<Appeal> getAppealsByUsername(String username);

    List<Appeal> getAppealsByUserType(UserType userType);

    List<Appeal> getAppealsByUserStatus(UserStatus userStatus);
    // 根据用户名查询用户
    List<User> getUsersByUsername(String username);
    // 根据用户类型查询用户
    List<User> getUsersByUserType(UserType userType);
    // 根据账号状态查询用户
    List<User> getUsersByUserStatus(UserStatus userStatus);

    // 根据用户名查询评论
    List<Comment> getCommentsByUsername(String username);
    // 根据用户类型查询评论
    List<Comment> getCommentsByUserType(UserType userType);
    // 根据账号状态查询评论
    List<Comment> getCommentsByUserStatus(UserStatus userStatus);
}