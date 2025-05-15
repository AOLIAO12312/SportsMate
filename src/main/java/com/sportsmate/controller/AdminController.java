package com.sportsmate.controller;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.UserType;
import com.sportsmate.service.AdminService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    // 验证是否为管理员
    private boolean isAdmin() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null || claims.get("userType") == null) {
            return false;
        }
        try {
            UserType type = UserType.valueOf((String) claims.get("userType"));
            return type == UserType.管理员;
        } catch (IllegalArgumentException e) {
            logger.error("用户类型解析失败", e);
            return false;
        }
    }

    // 查看举报
    @GetMapping("/getReports")
    public Result getReports() {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Report> reports = adminService.getReports();
            return Result.success(reports);
        } catch (Exception e) {
            logger.error("获取举报列表失败", e);
            return Result.error("获取举报列表失败");
        }
    }

    // 查看申诉
    @GetMapping("/getAppeals")
    public Result getAppeals() {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Appeal> appeals = adminService.getAppeals();
            return Result.success(appeals);
        } catch (Exception e) {
            logger.error("获取申诉列表失败", e);
            return Result.error("获取申诉列表失败");
        }
    }

    // 管理员处理举报
    @PostMapping("/handleReport")
    public Result handleReport(@RequestParam Integer reportId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            adminService.handleReport(reportId);
            return Result.success();
        } catch (Exception e) {
            logger.error("处理举报失败，reportId: {}", reportId, e);
            return Result.error("处理举报失败");
        }
    }

    // 管理员处理申诉
    @PostMapping("/handleAppeal")
    public Result handleAppeal(@RequestParam Integer appealId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            adminService.handleAppeal(appealId);
            return Result.success();
        } catch (Exception e) {
            logger.error("处理申诉失败，appealId: {}", appealId, e);
            return Result.error("处理申诉失败");
        }
    }

    // 管理员封禁用户
    @PostMapping("/banUser")
    public Result banUser(@RequestParam Integer userId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            adminService.banUser(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("封禁用户失败，userId: {}", userId, e);
            return Result.error("封禁用户失败");
        }
    }

    // 管理员解封用户
    @PostMapping("/unbanUser")
    public Result unbanUser(@RequestParam Integer userId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            adminService.unbanUser(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("解封用户失败，userId: {}", userId, e);
            return Result.error("解封用户失败");
        }
    }
}