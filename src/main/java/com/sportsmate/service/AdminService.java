package com.sportsmate.service;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Report;
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
}