package com.sportsmate.controller;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.Comment;
import com.sportsmate.service.AdminService;
import com.sportsmate.service.UserService;
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

    @Autowired
    private UserService userService;

    // 验证是否为管理员(token无用户类型，应该是先在token中获取id，然后通过id获取用户类型)
    private boolean isAdmin() {
        Logger logger = LoggerFactory.getLogger(AdminController.class);
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (claims == null) {
            logger.error("ThreadLocalUtil.get() 返回 null，无法获取用户信息");
            return false;
        }
        Object idObj = claims.get("id");
        if (idObj == null) {
            logger.error("claims 中没有包含 id 信息");
            return false;
        }
        Integer userId = (Integer) idObj;
        // 通过用户 ID 获取用户信息
        User user = userService.findByUserId(userId);
        if (user == null) {
            logger.error("根据 userId: {} 未找到用户信息", userId);
            return false;
        }
        if (user.getUserType() == null) {
            logger.error("用户 userId: {} 的 userType 为 null", userId);
            return false;
        }
        return user.getUserType() == UserType.管理员;
    }

    // 查看举报列表
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

    // 查看申诉列表
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
    public Result handleReport(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer reportId = params.get("reportId");
            if (reportId == null) {
                return Result.error("未提供有效的举报ID");
            }
            Report report = adminService.getReportById(reportId);
            if (report == null) {
                return Result.error("未找到该举报信息，无法处理");
            }
            adminService.handleReport(reportId);
            return Result.success();
        } catch (Exception e) {
            logger.error("处理举报失败", e);
            return Result.error("处理举报失败");
        }
    }

    // 管理员处理申诉
    @PostMapping("/handleAppeal")
    public Result handleAppeal(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer appealId = params.get("appealId");
            if (appealId == null) {
                return Result.error("未提供有效的申诉ID");
            }
            adminService.handleAppeal(appealId);
            return Result.success();
        } catch (Exception e) {
            logger.error("处理申诉失败", e);
            return Result.error("处理申诉失败");
        }
    }

    // 管理员封禁用户
    @PostMapping("/banUser")
    public Result banUser(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer userId = params.get("userId");
            logger.info("接收到的用户ID: {}", userId);
            if (userId == null) {
                return Result.error("未提供有效的用户ID");
            }
            adminService.banUser(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("封禁用户失败", e);
            return Result.error("封禁用户失败");
        }
    }

    // 管理员解封用户
    @PostMapping("/unbanUser")
    public Result unbanUser(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer userId = params.get("userId");
            if (userId == null) {
                return Result.error("未提供有效的用户ID");
            }
            adminService.unbanUser(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("解封用户失败", e);
            return Result.error("解封用户失败");
        }
    }

    // 查看单个举报
    @GetMapping("/getReportById")
    public Result getReportById(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer reportId = params.get("reportId");
            if (reportId == null) {
                return Result.error("未提供有效的举报ID");
            }
            Report report = adminService.getReportById(reportId);
            if (report == null) {
                return Result.error("未找到该举报信息");
            }
            return Result.success(report);
        } catch (Exception e) {
            logger.error("获取单个举报失败", e);
            return Result.error("获取单个举报失败");
        }
    }

    // 查看单个申诉
    @GetMapping("/getAppealById")
    public Result getAppealById(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer appealId = params.get("appealId");
            if (appealId == null) {
                return Result.error("未提供有效的申诉ID");
            }
            Appeal appeal = adminService.getAppealById(appealId);
            if (appeal == null) {
                return Result.error("未找到该申诉信息");
            }
            return Result.success(appeal);
        } catch (Exception e) {
            logger.error("获取单个申诉失败", e);
            return Result.error("获取单个申诉失败");
        }
    }

    @GetMapping("/getReportsByUsername")
    public Result getReportsByUsername(@RequestParam String username) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Report> reports = adminService.getReportsByUsername(username);
            return Result.success(reports);
        } catch (Exception e) {
            logger.error("根据用户名获取举报列表失败", e);
            return Result.error("根据用户名获取举报列表失败");
        }
    }

    @GetMapping("/getReportsByUserType")
    public Result getReportsByUserType(@RequestParam UserType userType) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Report> reports = adminService.getReportsByUserType(userType);
            return Result.success(reports);
        } catch (Exception e) {
            logger.error("根据用户类型获取举报列表失败", e);
            return Result.error("根据用户类型获取举报列表失败");
        }
    }

    @GetMapping("/getReportsByUserStatus")
    public Result getReportsByUserStatus(@RequestParam UserStatus userStatus) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Report> reports = adminService.getReportsByUserStatus(userStatus);
            return Result.success(reports);
        } catch (Exception e) {
            logger.error("根据用户状态获取举报列表失败", e);
            return Result.error("根据用户状态获取举报列表失败");
        }
    }

    @GetMapping("/getAppealsByUsername")
    public Result getAppealsByUsername(@RequestParam String username) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Appeal> appeals = adminService.getAppealsByUsername(username);
            return Result.success(appeals);
        } catch (Exception e) {
            logger.error("根据用户名获取申诉列表失败", e);
            return Result.error("根据用户名获取申诉列表失败");
        }
    }

    @GetMapping("/getAppealsByUserType")
    public Result getAppealsByUserType(@RequestParam UserType userType) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Appeal> appeals = adminService.getAppealsByUserType(userType);
            return Result.success(appeals);
        } catch (Exception e) {
            logger.error("根据用户类型获取申诉列表失败", e);
            return Result.error("根据用户类型获取申诉列表失败");
        }
    }

    @GetMapping("/getAppealsByUserStatus")
    public Result getAppealsByUserStatus(@RequestParam UserStatus userStatus) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Appeal> appeals = adminService.getAppealsByUserStatus(userStatus);
            return Result.success(appeals);
        } catch (Exception e) {
            logger.error("根据用户状态获取申诉列表失败", e);
            return Result.error("根据用户状态获取申诉列表失败");
        }
    }

    @GetMapping("/getUsersByUsername")
    public Result getUsersByUsername(@RequestParam String username) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<User> users = adminService.getUsersByUsername(username);
            return Result.success(users);
        } catch (Exception e) {
            logger.error("根据用户名查询用户失败", e);
            return Result.error("根据用户名查询用户失败");
        }
    }

    // 根据用户类型查询用户
    @GetMapping("/getUsersByUserType")
    public Result getUsersByUserType(@RequestParam UserType userType) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<User> users = adminService.getUsersByUserType(userType);
            return Result.success(users);
        } catch (Exception e) {
            logger.error("根据用户类型查询用户失败", e);
            return Result.error("根据用户类型查询用户失败");
        }
    }

    // 根据账号状态查询用户
    @GetMapping("/getUsersByUserStatus")
    public Result getUsersByUserStatus(@RequestParam UserStatus userStatus) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<User> users = adminService.getUsersByUserStatus(userStatus);
            return Result.success(users);
        } catch (Exception e) {
            logger.error("根据账号状态查询用户失败", e);
            return Result.error("根据账号状态查询用户失败");
        }
    }

    // 根据用户名查询评论
    @GetMapping("/getCommentsByUsername")
    public Result getCommentsByUsername(@RequestParam String username) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Comment> comments = adminService.getCommentsByUsername(username);
            return Result.success(comments);
        } catch (Exception e) {
            logger.error("根据用户名查询评论失败", e);
            return Result.error("根据用户名查询评论失败");
        }
    }

    // 根据用户类型查询评论
    @GetMapping("/getCommentsByUserType")
    public Result getCommentsByUserType(@RequestParam UserType userType) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Comment> comments = adminService.getCommentsByUserType(userType);
            return Result.success(comments);
        } catch (Exception e) {
            logger.error("根据用户类型查询评论失败", e);
            return Result.error("根据用户类型查询评论失败");
        }
    }

    // 根据账号状态查询评论
    @GetMapping("/getCommentsByUserStatus")
    public Result getCommentsByUserStatus(@RequestParam UserStatus userStatus) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            List<Comment> comments = adminService.getCommentsByUserStatus(userStatus);
            return Result.success(comments);
        } catch (Exception e) {
            logger.error("根据账号状态查询评论失败", e);
            return Result.error("根据账号状态查询评论失败");
        }
    }

    @PostMapping("/warnUser")
    public Result warnUser(@RequestBody Map<String, Integer> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer userId = params.get("userId");
            if (userId == null) {
                return Result.error("未提供有效的用户ID");
            }
            adminService.warnUser(userId);
            return Result.success();
        } catch (Exception e) {
            logger.error("警告用户失败", e);
            return Result.error("警告用户失败");
        }
    }
}