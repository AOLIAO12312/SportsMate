package com.sportsmate.controller;

import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Comment;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.User;
import com.sportsmate.service.CommentService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody Comment comment) {
        commentService.addComment(comment);
        commentService.addCommentAndCheckMatchStatus(comment);
        return Result.success();
    }

    @DeleteMapping("/deleteComment")
    public Result delete(@RequestBody Map<String, Integer> requestBody) {
        Integer id = requestBody.get("id");
        if (id != null) {
            commentService.deleteComment(id);
            return Result.success();
        }
        return Result.error("未提供有效的id");
    }

    // 修改评论
    @PostMapping("/updateComment")
    public Result update(@RequestBody Comment comment) {
        commentService.updateComment(comment);
        return Result.success();
    }

    // 获取评论信息
    @GetMapping("/get")
    public Result get(@RequestParam Integer id) {
        if (id == null) {
            return Result.error("id不能为空");
        }
        Comment comment = commentService.getCommentById(id);
        return Result.success(comment);
    }






}
