package com.sportsmate.controller;

import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/coach")
public class CoachController {

    @Autowired
    private CoachProfileService coachProfileService;

    @PostMapping("/updateProfile")
    public Result updateCoachProfile(@RequestBody CoachProfile profile) {
        coachProfileService.updateProfile(profile);
        return Result.success();
    }

    @GetMapping("/profile")
    public Result getCoachProfile() {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        CoachProfile profile = coachProfileService.findByUserId(loginUserId);
        return Result.success(profile);
    }
}