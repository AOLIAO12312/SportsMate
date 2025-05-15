package com.sportsmate.controller;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.service.CommentService;
import com.sportsmate.service.MatchService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/request")
    private Result request(@RequestBody MatchRequestDTO dto){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        MatchRequest existingRequest = matchService.findActiveRequestByUserId(loginUserId);
        if (existingRequest != null) {
            return Result.error("当前存在未匹配的请求");
        }
        List<SuccessfulMatch> existingMatches = matchService.findActiveMatchByUserId(loginUserId);

//        for(SuccessfulMatch existingMatch : existingMatches){
//            if(commentService.findByMatchAndUserId(existingMatch.getId(),loginUserId) == null){
//                return Result.error("当前存在未完成(未评价)的比赛");
//            }
//        }

        matchService.addRequestWithAutoMatch(dto, loginUserId);
        return Result.success();
    }


    @GetMapping("/listRequests")
    private Result listRequests(Integer pageNum,Integer pageSize){
        PageBean<MatchRequestDTO> pb = matchService.listRequests(pageNum,pageSize);
        return Result.success(pb);
    }

    @PutMapping("/cancel")
    private Result cancel(Integer requestId,String remark){
        MatchRequest matchRequest = matchService.findById(requestId);
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        if(matchRequest == null || !Objects.equals(matchRequest.getUserId(), loginUserId)){
            return Result.error("不存在此请求记录");
        }
        matchService.cancel(requestId,remark);
        return Result.success();
    }

    @GetMapping("/listSuccessfulMatches")
    private Result listSuccessfulMatches(Integer pageNum,Integer pageSize){
        PageBean<SuccessfulMatchDTO> pb = matchService.listSuccessfulMatches(pageNum,pageSize);
        return Result.success(pb);
    }


}
