package com.sportsmate.service.impl;

import com.sportsmate.mapper.MatchCommentMapper;
import com.sportsmate.mapper.SuccessfulMatchMapper;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.SuccessfulMatchStatus;
import com.sportsmate.service.MatchCommentService;
import com.sportsmate.utils.SensitiveWordUtil;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
public class MatchCommentServiceImpl implements MatchCommentService {

    @Autowired
    MatchCommentMapper commentMapper;

    @Autowired
    SuccessfulMatchMapper successfulMatchMapper;

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
    }

    @Override
    public void deleteComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        MatchComment comment = new MatchComment();
        comment.setId(id);
        comment.setUserId(loginUserId);
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if (successfulMatch == null || (!successfulMatch.getUserId1().equals(loginUserId) && !successfulMatch.getUserId2().equals(loginUserId))) {
            throw new IllegalArgumentException("你没有权限对该比赛进行删除");
        }
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        commentMapper.deleteComment(id);
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
        commentMapper.updateComment(comment);
    }

    @Override
    public MatchComment getCommentById(Integer id) {
        return commentMapper.getCommentById(id);
    }

    @Override
    public MatchComment findByMatchAndUserId(Integer userId, Integer matchId) {
        return commentMapper.findByMatchAndUserId(userId, matchId);
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
        return commentMapper.getCommentsByUserId(userId);
    }
}