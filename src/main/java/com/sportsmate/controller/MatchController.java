package com.sportsmate.controller;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.MatchService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/request")
    private Result request(@RequestBody MatchRequestDTO dto){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        MatchRequest existing = matchService.findActiveRequestByUserId(loginUserId);
        if (existing != null) {
            return Result.error("当前存在未处理的请求");
        }

        matchService.addRequestWithAutoMatch(dto, loginUserId);
        return Result.success();
    }


    @GetMapping("/listRequests")
    private Result list(Integer pageNum,Integer pageSize){
        PageBean<MatchRequestDTO> pb = matchService.list(pageNum,pageSize);
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


}
