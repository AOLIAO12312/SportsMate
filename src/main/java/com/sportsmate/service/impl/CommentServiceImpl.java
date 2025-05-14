package com.sportsmate.service.impl;

import com.sportsmate.mapper.CommentMapper;
import com.sportsmate.pojo.Comment;
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
}
