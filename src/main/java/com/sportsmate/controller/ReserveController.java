package com.sportsmate.controller;

import com.sportsmate.dto.VenueDTO;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reserve")
public class ReserveController {

    //显示教练列表，按照运动、星级进行筛选
//    @GetMapping("/listCoach")
//    public Result listCoach(Integer pageNum,Integer pageSize){
//        PageBean<VenueDTO> pb =  venueService.list(pageNum,pageSize);
//        return Result.success(pb);
//    }

    //显示教练空闲时间，列表形式返回

    //预约教练，指定教练id和时间id

    //普通用户：返回预约记录

    //教练：返回申请预约记录

    //教练：接受/拒绝预约

    //
}
