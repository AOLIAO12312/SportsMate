package com.sportsmate.service.impl;

import com.sportsmate.mapper.CommentMapper;
import com.sportsmate.mapper.SuccessfulMatchMapper;
import com.sportsmate.pojo.Comment;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.SuccessfulMatchStatus;
import com.sportsmate.service.CommentService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    SuccessfulMatchMapper successfulMatchMapper;

    @Override
    public void addComment(Comment comment) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        comment.setUserId(loginUserId);
        comment.setCreatedAt(LocalDateTime.now());
        commentMapper.addComment(comment);
    }

    @Override
    public void deleteComment(Integer id) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        Comment comment = new Comment();
        comment.setId(id);
        comment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能删除自己的评论
        commentMapper.deleteComment(id);
    }

    @Override
    public void updateComment(Comment comment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        comment.setUserId(loginUserId);
        // 这里可以添加权限判断逻辑，确保只能修改自己的评论
        commentMapper.updateComment(comment);
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentMapper.getCommentById(id);
    }

    @Override
    public Comment findByMatchAndUserId(Integer userId, Integer matchId) {
        return commentMapper.findByMatchAndUserId(userId, matchId);
    }

    @Override
    public void CheckMatchStatus(Comment comment) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        comment.setUserId(loginUserId);
        comment.setCreatedAt(LocalDateTime.now());
        // 获取当前评论对应的匹配信息
        SuccessfulMatch successfulMatch = successfulMatchMapper.findById(comment.getMatchId());
        if (successfulMatch != null) {
            Integer opponentUserId = successfulMatch.getUserId1().equals(loginUserId) ? successfulMatch.getUserId2() : successfulMatch.getUserId1();
            // 检查另一个用户是否已经对该匹配进行了评论
            Comment opponentComment = findByMatchAndUserId(opponentUserId, comment.getMatchId());
            if (opponentComment != null) {
                // 如果另一个用户也完成了评论，则将匹配状态修改为已完成
                successfulMatch.setStatus(SuccessfulMatchStatus.已完成);
                successfulMatchMapper.updateStatus(successfulMatch.getId(), successfulMatch.getStatus());
            }
        }
    }
}