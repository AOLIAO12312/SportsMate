package com.sportsmate.service.impl;

import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.mapper.CoachReservationMapper;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationStatus;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.service.ReservationCommentService;
import com.sportsmate.utils.SensitiveWordUtil;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
public class ReservationCommentServiceImpl implements ReservationCommentService {

    @Autowired
    ReservationCommentMapper coachCommentMapper;

    @Autowired
    CoachReservationMapper coachReservationMapper;

    @Override
    public void addCoachComment(ReservationComment coachComment) {
        if (SensitiveWordUtil.containsForbiddenKeyword(coachComment.getCoachComment())) {
            throw new IllegalArgumentException("有违禁词，请重新输入");
        }
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        coachComment.setCreatedAt(LocalDateTime.now());

        // 通过 coachReservationId 获取 CoachReservation 对象
        CoachReservation coachReservation = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation == null ) {
            throw new IllegalArgumentException("未找到对应的预约记录");
        }
        if(coachReservation.getStatus() != ReservationStatus.预约成功){
            throw new IllegalArgumentException("当前不处于可评价状态");
        }

        // 从 CoachReservation 对象中获取 coachId 并设置到 ReservationComment 中
        coachComment.setCoachId(coachReservation.getCoachId());

        // 检查是否已有评论
        if (checkComment(coachComment.getCoachReservationId())) {
            throw new IllegalArgumentException("已有评论，请勿重复提交");
        }
        CoachReservation coachReservation1 = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation1 == null || (!coachReservation1.getUserId().equals(loginUserId) )) {
            throw new IllegalArgumentException("你没有权限对该预约进行评论");
        }
        coachCommentMapper.addCoachComment(coachComment);
        handleCommentAndUpdateReservationStatus(coachComment);
    }


    @Override
    public void deleteCoachComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        ReservationComment coachComment = new ReservationComment();
        coachComment.setId(id);
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        CoachReservation coachReservation1 = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation1 == null || (!coachReservation1.getUserId().equals(loginUserId) )) {
            throw new IllegalArgumentException("你没有权限对该预约进行删除");
        }
        coachCommentMapper.deleteCoachComment(id);
    }

    @Override
    public void updateCoachComment(ReservationComment coachComment) {
        if (SensitiveWordUtil.containsForbiddenKeyword(coachComment.getCoachComment())) {
            throw new IllegalArgumentException("有违禁词，请重新输入");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能修改自己的评论
        CoachReservation coachReservation1 = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation1 == null || (!coachReservation1.getUserId().equals(loginUserId) )) {
            throw new IllegalArgumentException("你没有权限对该预约进行更新");
        }
        coachCommentMapper.updateCoachComment(coachComment);
    }

    @Override
    public ReservationComment getCoachCommentById(Integer id) {
        return coachCommentMapper.getCoachCommentById(id);
    }

    @Override
    public ReservationComment findByUserAndCoachId(Integer userId, Integer reservationId) {
        return coachCommentMapper.findByUserAndCoachId(userId, reservationId);
    }

    @Override
    public void handleCommentAndUpdateReservationStatus(ReservationComment coachComment) {
        CoachReservation coachReservation = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation != null) {
            coachReservation.setStatus(ReservationStatus.已完成);
            coachReservationMapper.setStatus(coachReservation);
        }
    }

    @Override
    public boolean checkComment(Integer coachReservationId) {
        // 根据 coachReservationId 查询是否存在评论
        ReservationComment comment = coachCommentMapper.findByCoachReservationId(coachReservationId);
        return comment != null;
    }

    @Override
    public List<ReservationComment> getCommentsByUserId(Integer userId) {
        return coachCommentMapper.getCommentsByUserId(userId);
    }
}
