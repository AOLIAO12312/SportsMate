package com.sportsmate.service;

import com.sportsmate.pojo.MatchComment;
import java.util.List;
public interface MatchCommentService {
    //添加评论
    void addComment(MatchComment comment);

    // 删除评论
    void deleteComment(Integer id);

    // 修改评论
    void updateComment(MatchComment comment);

    // 获取评论信息
    MatchComment getCommentById(Integer id);

    //通过userID和matchID获取comment
    MatchComment findByMatchAndUserId(Integer userId, Integer matchId);

    void CheckMatchStatus(MatchComment comment);

    // 获取用户发布的所有比赛评论
    List<MatchComment> getCommentsByUserId(Integer userId);


}