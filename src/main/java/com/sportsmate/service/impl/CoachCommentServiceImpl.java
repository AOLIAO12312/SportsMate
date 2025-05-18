package com.sportsmate.service.impl;

import com.sportsmate.mapper.CoachCommentMapper;
import com.sportsmate.mapper.CoachReservationMapper;
import com.sportsmate.pojo.CoachComment;
import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationStatus;
import com.sportsmate.service.CoachCommentService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CoachCommentServiceImpl implements CoachCommentService {

    @Autowired
    CoachCommentMapper coachCommentMapper;

    @Autowired
    CoachReservationMapper coachReservationMapper;

    @Override
    public void addCoachComment(CoachComment coachComment) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        coachComment.setCreatedAt(LocalDateTime.now());
        coachCommentMapper.addCoachComment(coachComment);
        handleCommentAndUpdateReservationStatus(coachComment);
    }

    @Override
    public void deleteCoachComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        CoachComment coachComment = new CoachComment();
        coachComment.setId(id);
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        coachCommentMapper.deleteCoachComment(id);
    }

    @Override
    public void updateCoachComment(CoachComment coachComment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能修改自己的评论
        coachCommentMapper.updateCoachComment(coachComment);
    }

    @Override
    public CoachComment getCoachCommentById(Integer id) {
        return coachCommentMapper.getCoachCommentById(id);
    }

    @Override
    public CoachComment findByUserAndCoachId(Integer userId, Integer reservationId) {
        return coachCommentMapper.findByUserAndCoachId(userId, reservationId);
    }

    @Override
    public void handleCommentAndUpdateReservationStatus(CoachComment coachComment) {
        CoachReservation coachReservation = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation != null) {
            coachReservation.setStatus(ReservationStatus.已完成);
            coachReservationMapper.setStatus(coachReservation);
        }
    }
}