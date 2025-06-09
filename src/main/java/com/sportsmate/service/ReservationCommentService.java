package com.sportsmate.service;

import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.ReservationComment;
import java.util.List;

public interface ReservationCommentService {
    //添加教练评论
    void addCoachComment(ReservationComment coachComment);

    // 删除教练评论
    void deleteCoachComment(Integer id);

    // 修改教练评论
    void updateCoachComment(ReservationComment coachComment);

    // 获取教练评论信息
    ReservationComment getCoachCommentById(Integer id);

    //通过userID和reservation_ID获取coachComment
    ReservationComment findByUserAndCoachId(Integer userId, Integer reservationId);

    // 处理评论后更新预约状态
    void handleCommentAndUpdateReservationStatus(ReservationComment coachComment);

    //
    boolean checkComment(Integer coachReservationId);

    // 获取用户发布的所有预约评论
    List<ReservationComment> getCommentsByUserId(Integer userId);

    ReservationComment findByUserAndReservationId(Integer userId, Integer reservationId);
}