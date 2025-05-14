package com.sportsmate.service;

import com.sportsmate.pojo.Comment;

import java.util.List;

public interface CommentService {
    //添加评论
    void addComment(Comment comment);

    // 删除评论
    void deleteComment(Integer id);

    // 修改评论
    void updateComment(Comment comment);

    // 获取评论信息
    Comment getCommentById(Integer id);
}
