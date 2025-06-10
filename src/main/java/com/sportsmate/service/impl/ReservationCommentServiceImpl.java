package com.sportsmate.service.impl;

import com.sportsmate.mapper.*;
import com.sportsmate.pojo.Venue;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationStatus;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.MatchComment;
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

    @Autowired
    VenueMapper venueMapper;

    @Autowired
    MatchCommentMapper matchCommentMapper;

    @Autowired
    CoachProfileMapper coachProfileMapper;

    @Override
    public void addCoachComment(ReservationComment coachComment) {
        if (SensitiveWordUtil.containsForbiddenKeyword(coachComment.getCoachComment())) {
            coachCommentMapper.updateForbiddenCount();
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
        updateVenueRating(coachReservation.getVenueId());
    }


    @Override
    public void deleteCoachComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        ReservationComment coachComment = getCoachCommentById(id);
        coachComment.setId(id);
        coachComment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        CoachReservation coachReservation1 = coachReservationMapper.findById(coachComment.getCoachReservationId());
        if (coachReservation1 == null || (!coachReservation1.getUserId().equals(loginUserId) )) {
            throw new IllegalArgumentException("你没有权限对该预约进行删除");
        }
        coachCommentMapper.deleteCoachComment(id);
        updateVenueRating(coachReservation1.getVenueId());
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
        updateVenueRating(coachReservation1.getVenueId());
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
        List<ReservationComment> reservationComments = coachCommentMapper.getCommentsByUserId(userId);

        for(ReservationComment reservationComment:reservationComments){
            // 添加场馆名字和教练名字
            reservationComment.setCoachName(coachProfileMapper.findByUserId(reservationComment.getCoachId()).getRealName());
            CoachReservation coachReservation = coachReservationMapper.findById(reservationComment.getCoachReservationId());
            reservationComment.setVenueName(venueMapper.findById(coachReservation.getVenueId()).getName());
        }
        return coachCommentMapper.getCommentsByUserId(userId);
    }

    private void updateVenueRating(Integer venueId) {
        // 获取该场馆的所有 Reservation 评论
        List<ReservationComment> reservationComments = coachCommentMapper.getCommentsByVenueId(venueId);

        // 获取该场馆的所有 Match 评论
        List<MatchComment> matchComments = matchCommentMapper.getCommentsByVenueId(venueId);

        // 计算总评分和评论数量
        int totalRatings = 0;
        int commentCount = 0;

        // 累加 Reservation 评论的评分
        for (ReservationComment comment : reservationComments) {
            totalRatings += comment.getVenueRating();
            commentCount++;
        }

        // 累加 Match 评论的评分
        for (MatchComment comment : matchComments) {
            totalRatings += comment.getVenueRating();
            commentCount++;
        }

        // 计算平均评分
        if (commentCount > 0) {
            double averageRating = (double) totalRatings / commentCount;

            // 更新场馆评分
            Venue venue = venueMapper.findById(venueId);
            if (venue != null) {
                venue.setRating(averageRating);
                venueMapper.update(venue);
            }
        }
    }

    @Override
    public ReservationComment findByUserAndReservationId(Integer userId, Integer reservationId) {
        ReservationComment reservationComment = coachCommentMapper.findByUserAndReservationId(userId, reservationId);

        // 添加场馆名字和教练名字
        reservationComment.setCoachName(coachProfileMapper.findByUserId(reservationComment.getCoachId()).getRealName());
        CoachReservation coachReservation = coachReservationMapper.findById(reservationComment.getCoachReservationId());
        reservationComment.setVenueName(venueMapper.findById(coachReservation.getVenueId()).getName());
        return reservationComment;
    }
}
