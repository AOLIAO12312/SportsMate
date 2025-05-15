package com.sportsmate.controller;

import com.sportsmate.converter.CoachProfileConverter;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Result;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/coach")
@Validated
public class CoachController {

    @Autowired
    private CoachProfileService coachProfileService;

    @Autowired
    private CoachProfileConverter coachProfileConverter;

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
}