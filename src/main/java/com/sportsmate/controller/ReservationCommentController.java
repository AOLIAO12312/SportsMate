package com.sportsmate.controller;

import com.sportsmate.pojo.CoachReservation;
import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.ReservationCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.sportsmate.utils.ThreadLocalUtil;

import java.util.Map;

@RestController
@RequestMapping("/reservationComment")
public class ReservationCommentController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationCommentController.class);

    @Autowired
    private ReservationCommentService coachCommentService;

    @PostMapping("/add")
    public Result add(@RequestBody ReservationComment coachComment) {
        logger.info("接收到的评论数据: {}", coachComment);

        try {
            coachCommentService.addCoachComment(coachComment);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
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
    public Result update(@RequestBody ReservationComment coachComment) {
        coachCommentService.updateCoachComment(coachComment);
        return Result.success();
    }

    // 获取教练评论信息z
    @GetMapping("/get")
    public Result get(@RequestParam(required = false) Integer reservationId ) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        List<ReservationComment> comments = coachCommentService.getCommentsByUserId(loginUserId);

        if (reservationId != null) {
            // 当传入 reservationId 时，筛选该用户在特定预约的评论
            ReservationComment comment = coachCommentService.findByUserAndReservationId(loginUserId, reservationId);
            if (comment != null) {
                comments = List.of(comment);
            } else {
                comments = List.of();
            }
        } else {
            // 没有 reservationId 的话，获取该用户的所有评论
            comments = coachCommentService.getCommentsByUserId(loginUserId);
        }

        return Result.success(comments);
    }
}