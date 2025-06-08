package com.sportsmate.controller;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.UserWithCoachInfo;
import com.sportsmate.service.AdminService;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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


    @PostMapping("/handleReport")
    public Result handleReport(@RequestBody Map<String, Object> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer reportId = (Integer) params.get("reportId");
            String message = (String) params.get("message");
            if (reportId == null) {
                return Result.error("未提供有效的举报ID");
            }
            if (message == null) {
                return Result.error("未提供留言信息");
            }
            String resultMessage = adminService.handleReport(reportId, message);
            return Result.success(resultMessage);
        } catch (Exception e) {
            logger.error("处理举报失败", e);
            return Result.error("处理举报失败");
        }
    }

    // 管理员处理申诉
    @PostMapping("/handleAppeal")
    public Result handleAppeal(@RequestBody Map<String, Object> params) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            Integer appealId = (Integer) params.get("appealId");
            String message = (String) params.get("message");
            if (appealId == null) {
                return Result.error("未提供有效的申诉ID");
            }
            if (message == null) {
                return Result.error("未提供留言信息");
            }
            String resultMessage = adminService.handleAppeal(appealId, message);
            return Result.success(resultMessage);
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
    public Result getReportById(@RequestParam Integer reportId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
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
    public Result getAppealById(@RequestParam Integer appealId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
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

    // 筛选用户
    @GetMapping("/getFilteredUsers")
    public Result getFilteredUsers(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) UserType userType,
                                   @RequestParam(required = false) UserStatus userStatus,
                                   @RequestParam(required = false) Integer userId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            PageBean<UserWithCoachInfo> pb = adminService.getFilteredUsers(pageNum, pageSize, username, userType, userStatus, userId);
            return Result.success(pb);
        } catch (Exception e) {
            logger.error("筛选用户失败", e);
            return Result.error("筛选用户失败");
        }
    }

    @GetMapping("/getFilteredMatchComments")
    public Result getFilteredMatchComments(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String username1,
                                           @RequestParam(required = false) String username2,
                                           @RequestParam(required = false) Integer matchId) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            PageBean<MatchComment> pb;
            if (username1 != null && username2 == null && matchId == null) {
                // 通过 username1 查询某个用户发起的所有评论
                pb = adminService.getMatchCommentsByUsername1(pageNum, pageSize, username1);
            } else if (username1 == null && username2 == null && matchId != null) {
                // 通过 match_id 查询比赛对应的下面的两条评论
                pb = adminService.getMatchCommentsByMatchId(pageNum, pageSize, matchId);
            } else if (username1 != null && username2 != null && matchId == null) {
                // 通过 username1 和 username2 查询两个用户一起打过的所有比赛的对应所有的比赛评论
                pb = adminService.getMatchCommentsByUsername1AndUsername2(pageNum, pageSize, username1, username2);
            } else {
                // 没有参数则返回所有 matchcomments
                pb = adminService.getAllMatchComments(pageNum, pageSize);
            }
            return Result.success(pb);
        } catch (Exception e) {
            logger.error("筛选比赛评论失败", e);
            return Result.error("筛选比赛评论失败");
        }
    }

    @GetMapping("/getFilteredReservationComments")
    public Result getFilteredReservationComments(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                 @RequestParam(required = false) String username,
                                                 @RequestParam(required = false) String coachName) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            PageBean<ReservationComment> pb;
            if (username != null && coachName == null) {
                // 通过 username 查询该用户发起的评论
                pb = adminService.getReservationCommentsByUsername(pageNum, pageSize, username);
            } else if (username == null && coachName != null) {
                // 通过 coachname 查询该教练接受的评论
                pb = adminService.getReservationCommentsByCoachname(pageNum, pageSize, coachName);
            } else if (username != null && coachName != null) {
                // 通过 username 和 coachname 筛选所有以该用户发起，该教练接受的评论
                pb = adminService.getReservationCommentsByUsernameAndCoachname(pageNum, pageSize, username, coachName);
            } else {
                // 没有参数则返回所有 reservationcomments
                pb = adminService.getAllReservationComments(pageNum, pageSize);
            }
            return Result.success(pb);
        } catch (Exception e) {
            logger.error("筛选预约评论失败", e);
            return Result.error("筛选预约评论失败");
        }
    }

    @GetMapping("/getFilteredReports")
    public Result getFilteredReports(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String reportername,
                                     @RequestParam(required = false) String reportedname,
                                     @RequestParam(required = false) HandleStatus status) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            PageBean<Report> pb;
            if (reportername != null && reportedname == null && status == null) {
                pb = adminService.getReportsByReportername(pageNum, pageSize, reportername);
            } else if (reportername == null && reportedname != null && status == null) {
                pb = adminService.getReportsByReportedname(pageNum, pageSize, reportedname);
            } else if (reportername == null && reportedname == null && status != null) {
                pb = adminService.getReportsByStatus(pageNum, pageSize, status);
            } else if (reportername != null && reportedname != null && status == null) {
                pb = adminService.getReportsByReporternameAndReportedname(pageNum, pageSize, reportername, reportedname);
            } else if (reportername != null && reportedname == null && status != null) {
                pb = adminService.getReportsByReporternameAndStatus(pageNum, pageSize, reportername, status);
            } else if (reportername == null && reportedname != null && status != null) {
                pb = adminService.getReportsByReportednameAndStatus(pageNum, pageSize, reportedname, status);
            } else if (reportername != null && reportedname != null && status != null) {
                pb = adminService.getReportsByReporternameReportednameAndStatus(pageNum, pageSize, reportername, reportedname, status);
            } else {
                pb = adminService.getAllReports(pageNum, pageSize);
            }
            return Result.success(pb);
        } catch (Exception e) {
            logger.error("筛选举报评论失败", e);
            return Result.error("筛选举报评论失败");
        }
    }

    @GetMapping("/getFilteredAppeals")
    public Result getFilteredAppeals(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false) String appellantname,
                                     @RequestParam(required = false) HandleStatus status) {
        try {
            if (!isAdmin()) {
                return Result.error("没有管理员权限");
            }
            PageBean<Appeal> pb;
            if (appellantname != null && status == null) {
                pb = adminService.getAppealsByAppellantname(pageNum, pageSize, appellantname);
            } else if (appellantname == null && status != null) {
                pb = adminService.getAppealsByStatus(pageNum, pageSize, status);
            } else if (appellantname != null && status != null) {
                pb = adminService.getAppealsByAppellantnameAndStatus(pageNum, pageSize, appellantname, status);
            } else {
                pb = adminService.getAllAppeals(pageNum, pageSize);
            }
            return Result.success(pb);
        } catch (Exception e) {
            logger.error("筛选申诉评论失败", e);
            return Result.error("筛选申诉评论失败");
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

    // 获取某一天的拦截次数（默认为今天）
    @GetMapping("/forbiddenCount")
    public Result<Integer> getForbiddenCount(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        int count = adminService.getCountByDate(date);
        return Result.success(count);
    }

    @GetMapping("/reportCount")
    public Result getReportCount(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        if (date == null) {
            date = LocalDate.now();
        }
        int count = adminService.getReportCountByDate(date);
        return Result.success(count);
    }
}