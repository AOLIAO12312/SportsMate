package com.sportsmate.service.impl;

import com.sportsmate.converter.CoachProfileConverter;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.mapper.MatchCommentMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.mapper.ReportMapper;
import com.sportsmate.mapper.AppealMapper;
import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.mapper.CoachProfileMapper;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.UserWithCoachInfo;
import com.github.pagehelper.Page;
import com.sportsmate.pojo.CoachProfile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sportsmate.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AppealMapper appealMapper;

    @Autowired
    private MatchCommentMapper matchCommentMapper;

    @Autowired
    private ReservationCommentMapper reservationCommentMapper;

    @Autowired
    private CoachProfileMapper coachProfileMapper;

    @Autowired
    private CoachProfileConverter coachProfileConverter;


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
    public PageBean<UserWithCoachInfo> getFilteredUsers(Integer pageNum, Integer pageSize, String username, UserType userType, UserStatus userStatus, Integer userId) {
        PageBean<UserWithCoachInfo> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.getUsersByFilters(username, userType, userStatus, userId);
        PageInfo<User> pageInfo = new PageInfo<>(users);

        List<UserWithCoachInfo> userWithCoachInfos = new ArrayList<>();
        for (User user : users) {
            UserWithCoachInfo userWithCoachInfo = new UserWithCoachInfo();
            userWithCoachInfo.setUser(user);
            if (user.getUserType() == UserType.教练) {
                CoachProfile coachProfile = coachProfileMapper.findByUserId(user.getId());
                if (coachProfile != null) {
                    CoachProfileDTO dto = coachProfileConverter.toDTO(coachProfile);
                    // CoachProfile 类中添加了 sportsName 字段
                    userWithCoachInfo.setCoachProfileDTO(dto);
                }
            }
            userWithCoachInfos.add(userWithCoachInfo);
        }
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(userWithCoachInfos);
        return pb;
    }

    @Override
    public PageBean<MatchComment> getMatchCommentsByUsername1(Integer pageNum, Integer pageSize, String username1) {
        PageBean<MatchComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MatchComment> comments = matchCommentMapper.getCommentsByUsername1(username1);
        PageInfo<MatchComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<MatchComment> getMatchCommentsByMatchId(Integer pageNum, Integer pageSize, Integer match_id) {
        PageBean<MatchComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MatchComment> comments = matchCommentMapper.getCommentsByMatchId(match_id);
        PageInfo<MatchComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<MatchComment> getMatchCommentsByUsername1AndUsername2(Integer pageNum, Integer pageSize, String username1, String username2) {
        PageBean<MatchComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MatchComment> comments = matchCommentMapper.getCommentsByUsername1AndUsername2(username1, username2);
        PageInfo<MatchComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<MatchComment> getAllMatchComments(Integer pageNum, Integer pageSize) {
        PageBean<MatchComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<MatchComment> comments = matchCommentMapper.getAllComments();
        PageInfo<MatchComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<ReservationComment> getReservationCommentsByUsername(Integer pageNum, Integer pageSize, String username) {
        PageBean<ReservationComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ReservationComment> comments = reservationCommentMapper.getCommentsByUsername(username);
        PageInfo<ReservationComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<ReservationComment> getReservationCommentsByCoachname(Integer pageNum, Integer pageSize, String coachname) {
        PageBean<ReservationComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ReservationComment> comments = reservationCommentMapper.getCommentsByCoachname(coachname);
        PageInfo<ReservationComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<ReservationComment> getReservationCommentsByUsernameAndCoachname(Integer pageNum, Integer pageSize, String username, String coachname) {
        PageBean<ReservationComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ReservationComment> comments = reservationCommentMapper.getCommentsByUsernameAndCoachname(username, coachname);
        PageInfo<ReservationComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<ReservationComment> getAllReservationComments(Integer pageNum, Integer pageSize) {
        PageBean<ReservationComment> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ReservationComment> comments = reservationCommentMapper.getAllComments();
        PageInfo<ReservationComment> pageInfo = new PageInfo<>(comments);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(comments);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReportername(Integer pageNum, Integer pageSize, String reportername) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReportername(reportername);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReportedname(Integer pageNum, Integer pageSize, String reportedname) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReportedname(reportedname);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByStatus(Integer pageNum, Integer pageSize, HandleStatus status) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByStatus(status);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReporternameAndReportedname(Integer pageNum, Integer pageSize, String reportername, String reportedname) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReporternameAndReportedname(reportername, reportedname);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReporternameAndStatus(Integer pageNum, Integer pageSize, String reportername, HandleStatus status) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReporternameAndStatus(reportername, status);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReportednameAndStatus(Integer pageNum, Integer pageSize, String reportedname, HandleStatus status) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReportednameAndStatus(reportedname, status);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getReportsByReporternameReportednameAndStatus(Integer pageNum, Integer pageSize, String reportername, String reportedname, HandleStatus status) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getReportsByReporternameReportednameAndStatus(reportername, reportedname, status);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Report> getAllReports(Integer pageNum, Integer pageSize) {
        PageBean<Report> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Report> reports = reportMapper.getAllReports();
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(reports);
        return pb;
    }

    @Override
    public PageBean<Appeal> getAppealsByAppellantname(Integer pageNum, Integer pageSize, String appellantname) {
        PageBean<Appeal> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Appeal> appeals = appealMapper.getAppealsByAppellantname(appellantname);
        PageInfo<Appeal> pageInfo = new PageInfo<>(appeals);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(appeals);
        return pb;
    }

    @Override
    public PageBean<Appeal> getAppealsByStatus(Integer pageNum, Integer pageSize, HandleStatus status) {
        PageBean<Appeal> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Appeal> appeals = appealMapper.getAppealsByStatus(status);
        PageInfo<Appeal> pageInfo = new PageInfo<>(appeals);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(appeals);
        return pb;
    }

    @Override
    public PageBean<Appeal> getAppealsByAppellantnameAndStatus(Integer pageNum, Integer pageSize, String appellantname, HandleStatus status) {
        PageBean<Appeal> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Appeal> appeals = appealMapper.getAppealsByAppellantnameAndStatus(appellantname, status);
        PageInfo<Appeal> pageInfo = new PageInfo<>(appeals);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(appeals);
        return pb;
    }

    @Override
    public PageBean<Appeal> getAllAppeals(Integer pageNum, Integer pageSize) {
        PageBean<Appeal> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Appeal> appeals = appealMapper.getAllAppeals();
        PageInfo<Appeal> pageInfo = new PageInfo<>(appeals);
        pb.setTotal(pageInfo.getTotal());
        pb.setItems(appeals);
        return pb;
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

    @Override
    public int getCountByDate(LocalDate date) {
        return reservationCommentMapper.getForbiddenCount(date);
    }

    @Override
    public int getReportCountByDate(LocalDate date) {
        return reportMapper.getReportCountByDate(date);
    }
}