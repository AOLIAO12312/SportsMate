package com.sportsmate.controller;

import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.MatchCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/matchComment")
public class MatchCommentController {

    @Autowired
    private MatchCommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody MatchComment comment) {
        commentService.addComment(comment);
        commentService.CheckMatchStatus(comment);
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
    public Result update(@RequestBody MatchComment comment) {
        commentService.updateComment(comment);
        return Result.success();
    }

    // 获取评论信息
    @GetMapping("/get")
    public Result get(@RequestParam Integer id) {
        if (id == null) {
            return Result.error("id不能为空");
        }
        MatchComment comment = commentService.getCommentById(id);
        if(comment == null){
            return Result.error("未找到该评论");
        }
        return Result.success(comment);
    }
}
