package com.sportsmate.controller;

import com.sportsmate.pojo.Result;
import com.sportsmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sportsmate.pojo.User;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(String username,String passwd){
        //查询用户
        User u = userService.findByUserName(username);
        if(u == null){
            //注册
            userService.register(username,passwd);
            return Result.success();
        }else{
            return Result.error("用户名已被占用");
        }
    }

    @GetMapping("/info")
    public Result<User> info(@RequestParam Integer id){
        User u = userService.findByUserId(id);
        if(u == null){
            return Result.error("该用户未找到");
        }else{
            return Result.success(u);
        }
    }

}
