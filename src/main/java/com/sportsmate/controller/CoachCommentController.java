package com.sportsmate.controller;

import com.sportsmate.pojo.CoachComment;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.CoachCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/coachComment")
public class CoachCommentController {

    @Autowired
    private CoachCommentService coachCommentService;

    @PostMapping("/add")
    public Result add(@RequestBody CoachComment coachComment) {
        coachCommentService.addCoachComment(coachComment);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody Map<String, Integer> requestBody) {
        Integer id = requestBody.get("id");
        if (id != null) {
            coachCommentService.deleteCoachComment(id);
            return Result.success();
        }
        return Result.error("未提供有效的id");
    }

    // 修改教练评论
    @PostMapping("/update")
    public Result update(@RequestBody CoachComment coachComment) {
        coachCommentService.updateCoachComment(coachComment);
        return Result.success();
    }

    // 获取教练评论信息
    @GetMapping("/get")
    public Result get(@RequestParam Integer id) {
        if (id == null) {
            return Result.error("id不能为空");
        }
        CoachComment coachComment = coachCommentService.getCoachCommentById(id);
        if(coachComment == null){
            return Result.error("未找到该评论");
        }
        return Result.success(coachComment);
    }
}