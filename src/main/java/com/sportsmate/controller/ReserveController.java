package com.sportsmate.controller;

import com.sportsmate.converter.AvailableTimeConverter;
import com.sportsmate.dto.AvailableTimeDTO;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.pojo.*;
import com.sportsmate.service.AvailableTimeService;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.service.CoachReservationService;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Autowired
    private CoachProfileService coachProfileService;

    @Autowired
    private AvailableTimeService availableTimeService;

    @Autowired
    private AvailableTimeConverter availableTimeConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private CoachReservationService coachReservationService;


    //显示教练列表，按照运动
    @GetMapping("/listCoach")
    public Result listCoach(@RequestParam Integer pageNum, @RequestParam Integer pageSize, String sportName){
        PageBean<CoachProfileDTO> pb =  coachProfileService.listCoach(pageNum,pageSize,sportName);
        if(pb == null){
            return Result.error("不存在该运动");
        }
        return Result.success(pb);
    }

    //显示教练空闲时间，列表形式返回
    @GetMapping("/availableTime")
    public Result availableTime(@RequestParam Integer coachUserId){
        // 获取该教练已有的空闲时间记录
        User user = userService.findByUserId(coachUserId);
        if(user == null || user.getUserType() != UserType.教练){
            return Result.error("对应教练用户不存在");
        }

        List<AvailableTimeDTO> dtos = new ArrayList<>();
        for (AvailableTime availableTime : availableTimeService.findByUserId(coachUserId)) {
            AvailableTimeDTO dto = availableTimeConverter.toDTO(availableTime);
            dtos.add(dto);
        }
        return Result.success(dtos);
    }

    //预约教练，指定教练id和时间id

    @PostMapping("/requestReservation")
    public Result requestReservation(Integer coachUserId,Integer availableTimeId){
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        User user = userService.findByUserId(coachUserId);
        if(user == null || user.getUserType() != UserType.教练){
            return Result.error("对应教练用户不存在");
        }
        AvailableTime availableTime = availableTimeService.findById(availableTimeId);
        if(availableTime == null || !Objects.equals(availableTime.getCoachId(), coachUserId)){
            return Result.error("空闲时间不存在");
        }

        coachReservationService.requestReservation(loginUserId,availableTime);
        return Result.success();
    }

    //普通用户：返回预约记录
    @GetMapping("/listReservationUser")
    public Result listReservationUser(){
        //TODO:之后修改为分页查询
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        return Result.success(coachReservationService.findByUserId(loginUserId));
    }

    //教练：返回申请预约记录
    @GetMapping("/listReservationCoach")
    public Result listReservationCoach(){
        //TODO:之后修改为分页查询
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        User user = userService.findByUserId(loginUserId);
        if(user.getUserType() != UserType.教练){
            return Result.error("你没有查询权限");
        }
        return Result.success(coachReservationService.findByCoachId(loginUserId));
    }


    //教练：接受/拒绝预约
    @PostMapping("/acceptReservation")
    public Result acceptReservation(Integer reservationId){
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        CoachReservation coachReservation = coachReservationService.findById(reservationId);
        if(!Objects.equals(coachReservation.getCoachId(), loginUserId)){
            return Result.error("你没有该操作的权限");
        }
        if(coachReservation.getStatus() != ReservationStatus.待确认){
            return Result.error("当前预约非待确认状态");
        }
        coachReservation.setStatus(ReservationStatus.预约成功);
        coachReservationService.setStatus(coachReservation);
        return Result.success();
    }

    @PostMapping("/rejectReservation")
    public Result rejectReservation(Integer reservationId){
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        CoachReservation coachReservation = coachReservationService.findById(reservationId);
        if(!Objects.equals(coachReservation.getCoachId(), loginUserId)){
            return Result.error("你没有该操作的权限");
        }
        if(coachReservation.getStatus() != ReservationStatus.待确认){
            return Result.error("当前预约非待确认状态");
        }
        coachReservation.setStatus(ReservationStatus.已取消);
        coachReservationService.setStatus(coachReservation);
        return Result.success();
    }
}
