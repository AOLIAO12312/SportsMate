package com.sportsmate.controller;

import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.UserType;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.JwtUtil;
import com.sportsmate.utils.Md5Util;
import com.sportsmate.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sportsmate.pojo.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CoachProfileService coachProfileService;

    @PostMapping("/register")
    public Result register(String username,String passwd,String userType){
        UserType type;
        // 校验 userType 是否为合法枚举
        try {
            type = UserType.valueOf(userType);  // 若非法会抛异常
        } catch (IllegalArgumentException e) {
            return Result.error("无效的用户类型: " + userType);
        }

        if(type.equals(UserType.管理员)){
            return Result.error("禁止注册管理员账号");
        }

        //查询用户
        User u = userService.findByUserName(username);
        if(u == null){
            //注册
            userService.register(username,passwd,type);
            if(type.equals(UserType.教练)){
                User registerUser = userService.findByUserName(username);
                Integer registerUserId = registerUser.getId();
                coachProfileService.add(registerUserId);
            }
            return Result.success();
        }else{
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String passwd){
        //根据用户名查询用户
        User loginuser = userService.findByUserName(username);

        if(loginuser == null){
            return Result.error("用户名错误");
        }

        //判断密码是否正确
        if(Md5Util.getMD5String(passwd).equals(loginuser.getPassword())){
            //登录成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginuser.getId());
            claims.put("username",loginuser.getUsername());
            claims.put("userType", loginuser.getUserType().toString());
            claims.put("status", loginuser.getStatus().toString());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }


    @GetMapping("/info")
    public Result<User> info(){
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        User u = userService.findByUserId(id);
        if(u == null){
            return Result.error("该用户未找到");
        }else{
            return Result.success(u);
        }
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        Map<String,Object> claims = ThreadLocalUtil.get();
        if(user.getId() == claims.get("id")){
            userService.update(user);
            return Result.success();
        }else {
            return Result.error("id与token不一致");
        }
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params){
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");
        if(oldPwd == null || newPwd == null || rePwd == null){
            return Result.error("缺少必要参数");
        }
        Map<String,Object> claims = ThreadLocalUtil.get();
        User loginUser = userService.findByUserId((Integer) claims.get("id"));
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("旧密码输出错误");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次密码输入不一致");
        }
        userService.updatePwd(newPwd);
        return Result.success();
    }
}
