package com.sportsmate.controller;

import com.sportsmate.dto.VenueDTO;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.Venue;
import com.sportsmate.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venue")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping("/add")
    public Result add(@RequestBody @Validated Venue venue){
        if(venue==null){
            return Result.error("传入值为空");
        }
        Venue existingVenue = venueService.findByName(venue.getName());
        if(existingVenue!=null){
            return Result.error("该场馆已存在");
        }

        venueService.add(venue);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated Venue venue){
        if(venue == null){
            return Result.error("传入值为空");
        }
        if(venue.getId() == null){
            return Result.error("缺少场馆ID");
        }
        venueService.update(venue);
        return Result.success();
    }

    @GetMapping("/info")
    public Result info(@RequestParam String name){
        return Result.success(venueService.findByName(name));
    }

    @GetMapping("/list")
    public Result list(Integer pageNum,Integer pageSize){
        PageBean<VenueDTO> pb =  venueService.list(pageNum,pageSize);
        return Result.success(pb);
    }
}
