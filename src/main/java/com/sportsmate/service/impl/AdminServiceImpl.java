package com.sportsmate.service.impl;

import com.sportsmate.mapper.AdminMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Report> getReports() {
        try {
            return adminMapper.getAllReports();
        } catch (Exception e) {
            logger.error("获取举报列表失败", e);
            throw e;
        }
    }

    @Override
    public List<Appeal> getAppeals() {
        try {
            return adminMapper.getAllAppeals();
        } catch (Exception e) {
            logger.error("获取申诉列表失败", e);
            throw e;
        }
    }

    @Override
    public void handleReport(Integer reportId) {
        try {
            Report report = adminMapper.getReportById(reportId);
            if (report != null) {
                report.setStatus(HandleStatus.已处理);
                adminMapper.updateReportStatus(reportId, report.getStatus());
            }
        } catch (Exception e) {
            logger.error("处理举报失败，reportId: {}", reportId, e);
            throw e;
        }
    }


    @Override
    public void handleAppeal(Integer appealId) {
        try {
            Appeal appeal = adminMapper.getAppealById(appealId);
            if (appeal != null) {
                appeal.setStatus(HandleStatus.已处理);
                // 修改为传入正确的参数
                adminMapper.updateAppealStatus(appealId, appeal.getStatus());
            }
        } catch (Exception e) {
            logger.error("处理申诉失败，appealId: {}", appealId, e);
            throw e;
        }
    }

    @Override
    public void banUser(Integer userId) {
        try {
            User user = userMapper.findByUserId(userId);
            if (user != null) {
                user.setStatus(UserStatus.封禁);
                userMapper.update(user);
            }
        } catch (Exception e) {
            logger.error("封禁用户失败，userId: {}", userId, e);
            throw e;
        }
    }

    @Override
    public void unbanUser(Integer userId) {
        try {
            User user = userMapper.findByUserId(userId);
            if (user != null) {
                user.setStatus(UserStatus.正常);
                userMapper.update(user);
            }
        } catch (Exception e) {
            logger.error("解封用户失败，userId: {}", userId, e);
            throw e;
        }
    }

    @Override
    public Report getReportById(Integer reportId) {
        try {
            return adminMapper.getReportById(reportId);
        } catch (Exception e) {
            logger.error("获取单个举报失败，reportId: {}", reportId, e);
            throw e;
        }
    }

    @Override
    public Appeal getAppealById(Integer appealId) {
        try {
            return adminMapper.getAppealById(appealId);
        } catch (Exception e) {
            logger.error("获取单个申诉失败，appealId: {}", appealId, e);
            throw e;
        }
    }
}