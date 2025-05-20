package com.sportsmate.service.impl;

import com.sportsmate.mapper.CommentMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.mapper.ReportMapper;
import com.sportsmate.mapper.AppealMapper;
import com.sportsmate.pojo.Comment;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.Comment;
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
    private CommentMapper commentMapper;

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
    public List<Report> getReportsByUsername(String username) {
        User user = userMapper.findByUserName(username);
        if (user != null) {
            return reportMapper.getReportsByUserId(user.getId());
        }
        return null;
    }

    @Override
    public List<Report> getReportsByUserType(UserType userType) {
        return reportMapper.getReportsByUserType(userType);
    }

    @Override
    public List<Report> getReportsByUserStatus(UserStatus userStatus) {
        return reportMapper.getReportsByUserStatus(userStatus);
    }

    @Override
    public List<Appeal> getAppealsByUsername(String username) {
        User user = userMapper.findByUserName(username);
        if (user != null) {
            return appealMapper.getAppealsByUserId(user.getId());
        }
        return null;
    }

    @Override
    public List<Appeal> getAppealsByUserType(UserType userType) {
        return appealMapper.getAppealsByUserType(userType);
    }

    @Override
    public List<Appeal> getAppealsByUserStatus(UserStatus userStatus) {
        return appealMapper.getAppealsByUserStatus(userStatus);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        try {
            return userMapper.getUsersByUsername(username);
        } catch (Exception e) {
            logger.error("根据用户名查询用户失败", e);
            throw e;
        }
    }

    @Override
    public List<User> getUsersByUserType(UserType userType) {
        try {
            return userMapper.getUsersByUserType(userType);
        } catch (Exception e) {
            logger.error("根据用户类型查询用户失败", e);
            throw e;
        }
    }

    @Override
    public List<User> getUsersByUserStatus(UserStatus userStatus) {
        try {
            return userMapper.getUsersByUserStatus(userStatus);
        } catch (Exception e) {
            logger.error("根据账号状态查询用户失败", e);
            throw e;
        }
    }

    @Override
    public List<Comment> getCommentsByUsername(String username) {
        try {
            User user = userMapper.findByUserName(username);
            if (user != null) {
                return commentMapper.getCommentsByUserId(user.getId());
            }
            return null;
        } catch (Exception e) {
            logger.error("根据用户名查询评论失败", e);
            throw e;
        }
    }

    @Override
    public List<Comment> getCommentsByUserType(UserType userType) {
        try {
            List<User> users = userMapper.getUsersByUserType(userType);
            if (users != null && !users.isEmpty()) {
                return commentMapper.getCommentsByUserIds(users.stream().map(User::getId).toList());
            }
            return null;
        } catch (Exception e) {
            logger.error("根据用户类型查询评论失败", e);
            throw e;
        }
    }

    @Override
    public List<Comment> getCommentsByUserStatus(UserStatus userStatus) {
        try {
            List<User> users = userMapper.getUsersByUserStatus(userStatus);
            if (users != null && !users.isEmpty()) {
                return commentMapper.getCommentsByUserIds(users.stream().map(User::getId).toList());
            }
            return null;
        } catch (Exception e) {
            logger.error("根据账号状态查询评论失败", e);
            throw e;
        }
    }
}
