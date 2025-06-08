package com.sportsmate.controller;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.pojo.*;
import com.sportsmate.service.MatchCommentService;
import com.sportsmate.service.MatchService;
import com.sportsmate.service.VenueService;
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
    private MatchCommentService commentService;

    @Autowired
    private VenueService venueService;

    @PostMapping("/request")
    private Result request(@RequestBody MatchRequestDTO dto){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        MatchRequest existingRequest = matchService.findActiveRequestByUserId(loginUserId);
        if (existingRequest != null) {
            return Result.error("当前存在未匹配的请求");
        }
        List<SuccessfulMatch> existingMatches = matchService.findActiveMatchByUserId(loginUserId);

        for(SuccessfulMatch existingMatch : existingMatches){
            if(commentService.findByMatchAndUserId(loginUserId,existingMatch.getId()) == null){
                return Result.error("当前存在未完成(未评价)的比赛");
            }
        }
        Integer venueId = dto.getVenueId();
        if(venueId == null || venueService.findById(venueId) == null){
            return Result.error("该场馆不存在");
        }

        matchService.addRequestWithAutoMatch(dto, loginUserId);
        return Result.success();
    }


    @GetMapping("/listRequests")
    private Result listRequests(Integer pageNum,Integer pageSize,String status){
        PageBean<MatchRequestDTO> pb = matchService.listRequests(pageNum,pageSize,status);
        return Result.success(pb);
    }

    @PostMapping("/cancel")
    private Result cancel(Integer requestId,String remark){
        MatchRequest matchRequest = matchService.findById(requestId);
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        if(matchRequest == null || !Objects.equals(matchRequest.getUserId(), loginUserId)){
            return Result.error("不存在此请求记录");
        }
        if(matchRequest.getStatus() == MatchRequestStatus.已取消){
            return Result.error("该请求已被取消");
        }
        matchService.cancel(requestId,remark);
        return Result.success();
    }

    @GetMapping("/listSuccessfulMatches")
    private Result listSuccessfulMatches(Integer pageNum,Integer pageSize,String status){
        PageBean<SuccessfulMatchDTO> pb = matchService.listSuccessfulMatches(pageNum,pageSize,status);
        return Result.success(pb);
    }
}
