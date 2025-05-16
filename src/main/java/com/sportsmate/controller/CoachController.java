package com.sportsmate.controller;

import com.sportsmate.converter.AvailableTimeConverter;
import com.sportsmate.converter.CoachProfileConverter;
import com.sportsmate.dto.AvailableTimeDTO;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.pojo.AvailableTime;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Weekday;
import com.sportsmate.service.AvailableTimeService;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coach")
@Validated
public class CoachController {

    @Autowired
    private CoachProfileService coachProfileService;

    @Autowired
    private CoachProfileConverter coachProfileConverter;

    @Autowired
    private AvailableTimeService availableTimeService;

    @Autowired
    private AvailableTimeConverter availableTimeConverter;

    @PostMapping("/updateProfile")
    public Result updateCoachProfile(@RequestBody CoachProfileDTO dto) {
        CoachProfile coachProfile;
        try{
            coachProfile = coachProfileConverter.toEntity(dto);
        }catch (IllegalArgumentException e){
            return Result.error(e.getMessage());
        }
        coachProfileService.updateProfile(coachProfile);
        return Result.success();
    }

    @GetMapping("/profile")
    public Result getCoachProfile() {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        CoachProfile profile = coachProfileService.findByUserId(loginUserId);
        CoachProfileDTO dto = coachProfileConverter.toDTO(profile);
        return Result.success(dto);
    }

    @PostMapping("addAvailableTime")
    public Result addAvailableTime(@RequestBody AvailableTimeDTO dto) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        Weekday weekday = dto.getWeekday();
        LocalTime startTime = dto.getStartTime();
        LocalTime endTime = dto.getEndTime();

        // 获取该教练已有的空闲时间记录
        List<AvailableTime> existingAvailableTime = availableTimeService.findByUserId(loginUserId);

        if (!startTime.isBefore(endTime)) {
            return Result.error("开始时间必须早于结束时间");
        }

        Duration duration = Duration.between(startTime, endTime);
        if (duration.toHours() < 1) {
            return Result.error("时间段必须至少为1小时");
        }

        // 过滤出相同 weekday 下的时间段
        for (AvailableTime time : existingAvailableTime) {
            if (time.getWeekday() == weekday) {
                // 校验时间是否重叠
                if (startTime.isBefore(time.getEndTime()) && endTime.isAfter(time.getStartTime())) {
                    return Result.error("该时间段与已有空闲时间重叠，请重新填写");
                }
            }
        }

        // 添加空闲时间
        availableTimeService.addAvailableTime(loginUserId, weekday, startTime, endTime);
        return Result.success();
    }

    @GetMapping("/availableTimeInfo")
    public Result availableTimeInfo(){
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        // 获取该教练已有的空闲时间记录
        List<AvailableTimeDTO> dtos = new ArrayList<>();
        for (AvailableTime availableTime : availableTimeService.findByUserId(loginUserId)) {
            AvailableTimeDTO dto = availableTimeConverter.toDTO(availableTime);
            dtos.add(dto);
        }
        return Result.success(dtos);
    }
}