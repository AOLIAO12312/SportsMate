package com.sportsmate.service.impl;

import com.sportsmate.mapper.MatchCommentMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.mapper.ReportMapper;
import com.sportsmate.mapper.AppealMapper;
import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private ReportMapper reportMapper;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AppealMapper appealMapper;

    @Autowired
    private MatchCommentMapper commentMapper;

    @Autowired
    private ReservationCommentMapper reservationCommentMapper;

    @Override
    public List<Report> getReports() {
        try {
            return reportMapper.getAllReports();
        } catch (Exception e) {
            logger.error("获取举报列表失败", e);
            throw e;
        }
    }

    @Override
    public List<Appeal> getAppeals() {
        try {
            return appealMapper.getAllAppeals();
        } catch (Exception e) {
            logger.error("获取申诉列表失败", e);
            throw e;
        }
    }

    @Override
    public void handleReport(Integer reportId) {
        try {
            Report report = reportMapper.getReportById(reportId);
            if (report != null) {
                report.setStatus(HandleStatus.已处理);
                reportMapper.updateReportStatus(reportId, report.getStatus());
            }
        } catch (Exception e) {
            logger.error("处理举报失败，reportId: {}", reportId, e);
            throw e;
        }
    }


    @Override
    public void handleAppeal(Integer appealId) {
        try {
            Appeal appeal = appealMapper.getAppealById(appealId);
            if (appeal != null) {
                appeal.setStatus(HandleStatus.已处理);
                // 修改为传入正确的参数
                appealMapper.updateAppealStatus(appealId, appeal.getStatus());
            }
        } catch (Exception e) {
            logger.error("处理申诉失败，appealId: {}", appealId, e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void banUser(Integer userId) {
        try {
            // 直接调用 AdminMapper 中的 banUser 方法
            userMapper.banUser(userId);
            logger.info("用户 userId: {} 已被封禁", userId);
        } catch (Exception e) {
            logger.error("封禁用户失败，userId: {}", userId, e);
            throw new RuntimeException("封禁用户失败", e);
        }
    }


    @Override
    @Transactional
    public void unbanUser(Integer userId) {
        try {
            // 直接调用 AdminMapper 中的 banUser 方法
            userMapper.unbanUser(userId);
            logger.info("用户 userId: {} 已被解封", userId);
        } catch (Exception e) {
            logger.error("解封用户失败，userId: {}", userId, e);
            throw new RuntimeException("解封用户失败", e);
        }
    }

    @Override
    public Report getReportById(Integer reportId) {
        try {
            return reportMapper.getReportById(reportId);
        } catch (Exception e) {
            logger.error("获取单个举报失败，reportId: {}", reportId, e);
            throw e;
        }
    }

    @Override
    public Appeal getAppealById(Integer appealId) {
        try {
            return appealMapper.getAppealById(appealId);
        } catch (Exception e) {
            logger.error("获取单个申诉失败，appealId: {}", appealId, e);
            throw e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getFilteredItems(String type, String username, UserType userType, UserStatus userStatus) {
        logger.info("开始执行 getFilteredItems 方法，筛选类型: {}, 用户名: {}, 用户类型: {}, 用户状态: {}", type, username, userType, userStatus);
        try {
            switch (type) {
                case "reports":
                    logger.info("筛选类型为 reports，开始查询举报列表");
                    List<Report> reports = reportMapper.getReportsByFilters(username, userType, userStatus);
                    logger.info("成功查询到 {} 条举报记录", reports.size());
                    return (List<T>) reports;
                case "appeals":
                    logger.info("筛选类型为 appeals，开始查询申诉列表");
                    List<Appeal> appeals = appealMapper.getAppealsByFilters(username, userType, userStatus);
                    logger.info("成功查询到 {} 条申诉记录", appeals.size());
                    return (List<T>) appeals;
                case "users":
                    logger.info("筛选类型为 users，开始查询用户列表");
                    List<User> users = userMapper.getUsersByFilters(username, userType, userStatus);
                    logger.info("成功查询到 {} 条用户记录", users.size());
                    return (List<T>) users;
                case "matchComments":
                    logger.info("筛选类型为 matchComments，开始查询比赛评论列表");
                    List<MatchComment> matchComments = commentMapper.getCommentsByFilters(username, userType, userStatus);
                    logger.info("成功查询到 {} 条比赛评论记录", matchComments.size());
                    return (List<T>) matchComments;
                case "reservationComments":
                    logger.info("筛选类型为 reservationComments，开始查询预约评论列表");
                    List<ReservationComment> reservationComments = reservationCommentMapper.getCommentsByFilters(username, userType, userStatus);
                    logger.info("成功查询到 {} 条预约评论记录", reservationComments.size());
                    return (List<T>) reservationComments;
                default:
                    logger.error("无效的筛选类型: {}", type);
                    throw new IllegalArgumentException("无效的筛选类型: " + type);
            }
        } catch (Exception e) {
            logger.error("筛选过程中出现异常，筛选类型: {}, 用户名: {}, 用户类型: {}, 用户状态: {}", type, username, userType, userStatus, e);
            throw e;
        }
    }
    @Override
    @Transactional
    public void warnUser(Integer userId) {
        try {
            // 直接调用 UserMapper 中的 updateUserStatus 方法将用户状态设为警告
            userMapper.updateUserStatus(userId, UserStatus.警告);
            logger.info("用户 userId: {} 已被警告", userId);
        } catch (Exception e) {
            logger.error("警告用户失败，userId: {}", userId, e);
            throw new RuntimeException("警告用户失败", e);
        }
    }
}

