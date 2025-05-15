package com.sportsmate.controller;

import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.dto.VenueDTO;
import com.sportsmate.mapper.CoachProfileMapper;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.CoachProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private CoachProfileService coachProfileService;

    //显示教练列表，按照运动
    @GetMapping("/listCoach")
    public Result listCoach(@RequestBody Integer pageNum,@RequestBody Integer pageSize,String sportName){
        PageBean<CoachProfileDTO> pb =  coachProfileService.listCoach(pageNum,pageSize,sportName);
        if(pb == null){
            return Result.error("不存在该运动");
        }
        return Result.success(pb);
    }

    //显示教练空闲时间，列表形式返回

    //预约教练，指定教练id和时间id

    //普通用户：返回预约记录

    //教练：返回申请预约记录

    //教练：接受/拒绝预约

    //
}
