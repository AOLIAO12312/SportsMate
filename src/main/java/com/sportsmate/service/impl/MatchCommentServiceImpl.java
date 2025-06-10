package com.sportsmate.service.impl;

import com.sportsmate.mapper.*;
import com.sportsmate.pojo.*;
import com.sportsmate.service.MatchCommentService;
import com.sportsmate.utils.SensitiveWordUtil;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import java.util.Objects;

@Service
public class MatchCommentServiceImpl implements MatchCommentService {

    @Autowired
    MatchCommentMapper commentMapper;

    @Autowired
    SuccessfulMatchMapper successfulMatchMapper;

    @Autowired
    ReservationCommentMapper coachCommentMapper;



    @Autowired
    UserMapper userMapper;

    @Autowired
    VenueMapper venueMapper;

    @Override
    public void addComment(MatchComment comment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        comment.setUserId(loginUserId);
        comment.setCreatedAt(LocalDateTime.now());

        // 新添加的判断逻辑
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if (successfulMatch == null || (!successfulMatch.getUserId1().equals(loginUserId) && !successfulMatch.getUserId2().equals(loginUserId))) {
            throw new IllegalArgumentException("你没有权限对该比赛进行评论");
        }

        // 检查该比赛的该用户是否已经有评论
        MatchComment existingComment = commentMapper.findByMatchAndUserId(loginUserId, comment.getMatchId());
        if (existingComment != null) {
            throw new IllegalArgumentException("已有评论，请勿重复提交");
        }

        commentMapper.addComment(comment);
        User opponentUser = userMapper.findByUserId(Objects.equals(successfulMatch.getUserId1(), loginUserId) ? successfulMatch.getUserId2() : successfulMatch.getUserId1());
        Integer newRankScore = opponentUser.getRankScore();
        newRankScore += (comment.getOpponentRating() - 5) * 6;
        userMapper.setRankScore(opponentUser.getId(),newRankScore);
        Integer venueId = successfulMatch.getVenueId();
        // 更新场馆评分
        updateVenueRating(venueId);
    }

    @Override
    public void deleteComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        MatchComment comment = getCommentById(id);
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if(successfulMatch == null) {
            throw new IllegalArgumentException("没找到该比赛");
        }
        if (successfulMatch == null || (!successfulMatch.getUserId1().equals(loginUserId) && !successfulMatch.getUserId2().equals(loginUserId))) {
            throw new IllegalArgumentException("你没有权限对该比赛进行删除");
        }
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        commentMapper.deleteComment(id);
        Integer venueId = successfulMatch.getVenueId();
        updateVenueRating(venueId);
    }

    @Override
    public void updateComment(MatchComment comment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        comment.setUserId(loginUserId);
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if (successfulMatch == null || (!successfulMatch.getUserId1().equals(loginUserId) && !successfulMatch.getUserId2().equals(loginUserId))) {
            throw new IllegalArgumentException("你没有权限对该比赛进行更新");
        }
        MatchComment oldComment = commentMapper.getCommentById(comment.getId());
        if (oldComment == null) {
            throw new IllegalArgumentException("未找到该评论信息");
        }
        int ratingChange = comment.getOpponentRating() - oldComment.getOpponentRating();
        commentMapper.updateComment(comment);

        User opponentUser = userMapper.findByUserId(Objects.equals(successfulMatch.getUserId1(), loginUserId) ? successfulMatch.getUserId2() : successfulMatch.getUserId1());
        Integer newRankScore = opponentUser.getRankScore();
        newRankScore += ratingChange * 6;
        userMapper.setRankScore(opponentUser.getId(), newRankScore);
        Integer venueId = successfulMatch.getVenueId();
        updateVenueRating(venueId);
    }

    @Override
    public MatchComment getCommentById(Integer id) {
        return commentMapper.getCommentById(id);
    }

    @Override
    public MatchComment findByMatchAndUserId(Integer userId, Integer matchId) {
        MatchComment matchComment = commentMapper.findByMatchAndUserId(userId, matchId);

        // 添加对手用户名和场馆名字
        if(matchComment == null){
            return null;
        }
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(matchComment.getMatchId());
        User opponent;
        if(userId.equals(successfulMatch.getUserId1())){
            opponent = userMapper.findByUserId(successfulMatch.getUserId2());
        }else {
            opponent = userMapper.findByUserId(successfulMatch.getUserId1());
        }
        matchComment.setOpponentName(opponent.getUsername());
        matchComment.setVenueName(venueMapper.findById(successfulMatch.getVenueId()).getName());

        return matchComment;
    }

    @Override
    public void CheckMatchStatus(MatchComment comment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        comment.setUserId(loginUserId);
        comment.setCreatedAt(LocalDateTime.now());
        // 获取当前评论对应的匹配信息
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if (successfulMatch != null) {
            Integer opponentUserId = successfulMatch.getUserId1().equals(loginUserId) ? successfulMatch.getUserId2() : successfulMatch.getUserId1();
            // 检查另一个用户是否已经对该匹配进行了评论
            MatchComment opponentComment = findByMatchAndUserId(opponentUserId, comment.getMatchId());
            if (opponentComment != null) {
                // 如果另一个用户也完成了评论，则将匹配状态修改为已完成
                successfulMatch.setStatus(SuccessfulMatchStatus.已完成);
                successfulMatchMapper.updateStatus(successfulMatch.getId(), successfulMatch.getStatus());
            }
        }


    }
    @Override
    public List<MatchComment> getCommentsByUserId(Integer userId) {
        List<MatchComment> matchComments = commentMapper.getCommentsByUserId(userId);
        for(MatchComment matchComment:matchComments){
            // 添加对手用户名和场馆名字
            SuccessfulMatch successfulMatch = successfulMatchMapper.findById(matchComment.getMatchId());
            User opponent;
            if(userId.equals(successfulMatch.getUserId1())){
                opponent = userMapper.findByUserId(successfulMatch.getUserId2());
            }else {
                opponent = userMapper.findByUserId(successfulMatch.getUserId1());
            }
            matchComment.setOpponentName(opponent.getUsername());
            matchComment.setVenueName(venueMapper.findById(successfulMatch.getVenueId()).getName());
        }
        return matchComments;
    }

    private void updateVenueRating(Integer venueId) {
        // 获取该场馆的所有 Reservation 评论
        List<ReservationComment> reservationComments = coachCommentMapper.getCommentsByVenueId(venueId);

        // 获取该场馆的所有 Match 评论
        List<MatchComment> matchComments = commentMapper.getCommentsByVenueId(venueId);

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
}