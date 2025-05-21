package com.sportsmate.service.impl;

import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.mapper.CoachReservationMapper;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationStatus;
import com.sportsmate.service.ReservationCommentService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ReservationCommentServiceImpl implements ReservationCommentService {

    @Autowired
    ReservationCommentMapper coachCommentMapper;

    @Autowired
    CoachReservationMapper coachReservationMapper;

    @Override
    public void addCoachComment(ReservationComment coachComment) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        coachComment.setCreatedAt(LocalDateTime.now());

        // 确保 coach_id 不为空
        if (coachComment.getCoachId() == null) {
            throw new IllegalArgumentException("coach_id 不能为空");
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
        coachCommentMapper.deleteCoachComment(id);
    }

    @Override
    public void updateCoachComment(ReservationComment coachComment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能修改自己的评论
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
}