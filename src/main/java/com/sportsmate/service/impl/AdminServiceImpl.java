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
    private MatchCommentMapper matchCommentMapper;

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
    public String handleReport(Integer reportId, String message) {
        try {
            Report report = reportMapper.getReportById(reportId);
            if (report != null) {
                report.setStatus(HandleStatus.已处理);
                report.setReplyMessage(message); // 保存留言
                reportMapper.updateReportStatus(reportId, report.getStatus(), report.getReplyMessage());
                return "举报处理成功";
            }
            return "未找到该举报信息，无法处理";
        } catch (Exception e) {
            logger.error("处理举报失败，reportId: {}", reportId, e);
            throw e;
        }
    }


    @Override
    public String handleAppeal(Integer appealId, String message) {
        try {
            Appeal appeal = appealMapper.getAppealById(appealId);
            if (appeal != null) {
                appeal.setStatus(HandleStatus.已处理);
                // 假设Appeal类中有replyMessage字段，用于保存留言
                // 如果没有，需要在Appeal类中添加该字段
                appeal.setReplyMessage(message);
                appealMapper.updateAppealStatus(appealId, appeal.getStatus(),appeal.getReplyMessage());
                return "申诉处理成功";
            }
            return "未找到该申诉信息，无法处理";
        } catch (Exception e) {
            logger.error("处理申诉失败，appealId: {}", appealId, e);
            throw e;
        }
    }
    @Override
    @Transactional
    public void banUser(Integer userId) {
        try {
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
    public List<User> getFilteredUsers(String username, UserType userType, UserStatus userStatus) {
        try {
            return userMapper.getUsersByFilters(username, userType, userStatus);
        } catch (Exception e) {
            logger.error("筛选用户失败", e);
            throw e;
        }
    }

    @Override
    public List<MatchComment> getMatchCommentsByUsername1(String username1) {
        // 实现通过 username1 查询某个用户发起的所有评论的逻辑
        // 可能需要调用 MatchCommentMapper 进行查询
        return matchCommentMapper.getCommentsByUsername1(username1);
    }

    @Override
    public List<MatchComment> getMatchCommentsByMatchId(Integer match_id) {
        // 实现通过 match_id 查询比赛对应的下面的两条评论的逻辑
        return matchCommentMapper.getCommentsByMatchId(match_id);
    }

    @Override
    public List<MatchComment> getMatchCommentsByUsername1AndUsername2(String username1, String username2) {
        // 实现通过 username1 和 username2 查询两个用户一起打过的所有比赛的对应所有的比赛评论的逻辑
        return matchCommentMapper.getCommentsByUsername1AndUsername2(username1, username2);
    }

    @Override
    public List<MatchComment> getAllMatchComments() {
        // 实现返回所有比赛评论的逻辑
        return matchCommentMapper.getAllComments();
    }

    // AdminServiceImpl 实现类
    @Override
    public List<ReservationComment> getReservationCommentsByUsername(String username) {
        // 实现通过 username 查询该用户发起的评论的逻辑
        return reservationCommentMapper.getCommentsByUsername(username);
    }

    @Override
    public List<ReservationComment> getReservationCommentsByCoachname(String coachname) {
        // 实现通过 coachname 查询该教练接受的评论的逻辑
        return reservationCommentMapper.getCommentsByCoachname(coachname);
    }

    @Override
    public List<ReservationComment> getReservationCommentsByUsernameAndCoachname(String username, String coachname) {
        // 实现通过 username 和 coachname 筛选所有以该用户发起，该教练接受的评论的逻辑
        return reservationCommentMapper.getCommentsByUsernameAndCoachname(username, coachname);
    }

    @Override
    public List<ReservationComment> getAllReservationComments() {
        // 实现返回所有预约评论的逻辑
        return reservationCommentMapper.getAllComments();
    }

    @Override
    public List<Report> getReportsByReportername(String reportername) {
        // 实现通过 reportername 查询所有该用户发起的举报的逻辑
        return reportMapper.getReportsByReportername(reportername);
    }

    @Override
    public List<Report> getReportsByReportedname(String reportedname) {
        // 实现通过 reportedname 查询所有该用户被举报的举报的逻辑
        return reportMapper.getReportsByReportedname(reportedname);
    }

    @Override
    public List<Report> getReportsByStatus(HandleStatus status) {
        // 实现通过 status 查询所有该状态的举报的逻辑
        return reportMapper.getReportsByStatus(status);
    }

    @Override
    public List<Report> getReportsByReporternameAndReportedname(String reportername, String reportedname) {
        // 实现通过 reportername 和 reportedname 综合查询的逻辑
        return reportMapper.getReportsByReporternameAndReportedname(reportername, reportedname);
    }

    @Override
    public List<Report> getReportsByReporternameAndStatus(String reportername, HandleStatus status) {
        // 实现通过 reportername 和 status 综合查询的逻辑
        return reportMapper.getReportsByReporternameAndStatus(reportername, status);
    }

    @Override
    public List<Report> getReportsByReportednameAndStatus(String reportedname, HandleStatus status) {
        // 实现通过 reportedname 和 status 综合查询的逻辑
        return reportMapper.getReportsByReportednameAndStatus(reportedname, status);
    }

    @Override
    public List<Report> getReportsByReporternameReportednameAndStatus(String reportername, String reportedname, HandleStatus status) {
        // 实现通过 reportername、reportedname 和 status 综合查询的逻辑
        return reportMapper.getReportsByReporternameReportednameAndStatus(reportername, reportedname, status);
    }

    @Override
    public List<Report> getAllReports() {
        // 实现返回所有举报的逻辑
        return reportMapper.getAllReports();
    }

    // AdminServiceImpl 实现类
    @Override
    public List<Appeal> getAppealsByAppellantname(String appellantname) {
        // 实现通过 appellantname 查询所有该用户发起的申诉的逻辑
        return appealMapper.getAppealsByAppellantname(appellantname);
    }

    @Override
    public List<Appeal> getAppealsByStatus(HandleStatus status) {
        // 实现通过 status 查询所有该状态的申诉的逻辑
        return appealMapper.getAppealsByStatus(status);
    }

    @Override
    public List<Appeal> getAppealsByAppellantnameAndStatus(String appellantname, HandleStatus status) {
        // 实现通过 appellantname 和 status 综合查询的逻辑
        return appealMapper.getAppealsByAppellantnameAndStatus(appellantname, status);
    }

    @Override
    public List<Appeal> getAllAppeals() {
        // 实现返回所有申诉的逻辑
        return appealMapper.getAllAppeals();
    }

    @Override
    @Transactional
    public void warnUser(Integer userId) {
        try {
            userMapper.updateUserStatus(userId, UserStatus.警告);
            logger.info("用户 userId: {} 已被警告", userId);
        } catch (Exception e) {
            logger.error("警告用户失败，userId: {}", userId, e);
            throw new RuntimeException("警告用户失败", e);
        }
    }
}