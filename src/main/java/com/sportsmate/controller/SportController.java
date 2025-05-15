package com.sportsmate.controller;

import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Sport;
import com.sportsmate.service.SportService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sport")
public class SportController {

    @Autowired
    SportService sportService;

    @PostMapping("/add")
    public Result add(@Pattern(regexp = "^\\S{1,16}$")String sportName, String description){
        Sport sport = sportService.findByName(sportName);
        if (sport != null){
            return Result.error("该运动已存在");
        }
        sportService.add(sportName,description);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getSportNames(){
        List<String> names = sportService.getAllSportNames();
        return Result.success(names);
    }

    @GetMapping("/description")
    public Result getDescription(@RequestParam String sportName){
        String description = sportService.getDescription(sportName);
        if(description == null){
            return Result.error("该运动简介不存在");
        }
        return Result.success(description);
    }

    @PostMapping("update")
    public Result update(@RequestBody Sport sport){
        sportService.update(sport);
        return Result.success();
    }
}
