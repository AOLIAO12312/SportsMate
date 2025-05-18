package com.sportsmate.service;

import com.sportsmate.pojo.CoachComment;

import java.util.List;

public interface CoachCommentService {
    //添加教练评论
    void addCoachComment(CoachComment coachComment);

    // 删除教练评论
    void deleteCoachComment(Integer id);

    // 修改教练评论
    void updateCoachComment(CoachComment coachComment);

    // 获取教练评论信息
    CoachComment getCoachCommentById(Integer id);

    //通过userID和reservation_ID获取coachComment
    CoachComment findByUserAndCoachId(Integer userId, Integer reservationId);

    // 处理评论后更新预约状态
    void handleCommentAndUpdateReservationStatus(CoachComment coachComment);
}