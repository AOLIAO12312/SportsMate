package com.sportsmate.controller;

import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.mapper.SuccessfulMatchMapper;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.Result;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.AddressType;
import com.sportsmate.pojo.UserAddress;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.ReservationComment;
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
    @Autowired
    private ReservationCommentMapper reservationCommentMapper;
    @Autowired
    private SuccessfulMatchMapper successfulMatchMapper;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String passwd, String userType) {
        UserType type;
        // 校验 userType 是否为合法枚举
        try {
            type = UserType.valueOf(userType);  // 若非法会抛异常
        } catch (IllegalArgumentException e) {
            return Result.error("无效的用户类型: " + userType);
        }

        if (type.equals(UserType.管理员)) {
            return Result.error("禁止注册管理员账号");
        }

        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //注册
            userService.register(username, passwd, type);
            if (type.equals(UserType.教练)) {
                User registerUser = userService.findByUserName(username);
                Integer registerUserId = registerUser.getId();
                coachProfileService.add(registerUserId);
            }
            return Result.success();
        } else {
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String passwd) {
        //根据用户名查询用户
        User loginuser = userService.findByUserName(username);

        if (loginuser == null) {
            return Result.error("用户名错误");
        }

        //判断密码是否正确
        if (Md5Util.getMD5String(passwd).equals(loginuser.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginuser.getId());
            claims.put("username", loginuser.getUsername());
            claims.put("userType", loginuser.getUserType().toString());
            claims.put("status", loginuser.getStatus().toString());
            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }


    @GetMapping("/info")
    public Result<User> info() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        User u = userService.findByUserId(id);
        if (u == null) {
            return Result.error("该用户未找到");
        } else {
            return Result.success(u);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        if (user.getId() == claims.get("id")) {
            userService.update(user);
            return Result.success();
        } else {
            return Result.error("id与token不一致");
        }
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");
        String rePwd = params.get("rePwd");
        if (oldPwd == null || newPwd == null || rePwd == null) {
            return Result.error("缺少必要参数");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        User loginUser = userService.findByUserId((Integer) claims.get("id"));
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("旧密码输出错误");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码输入不一致");
        }
        userService.updatePwd(newPwd);
        return Result.success();
    }

    @PostMapping("/addAddress")
    public Result addAddress(@RequestBody Map<String, String> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        String country = params.get("country");
        String state = params.get("state");
        String city = params.get("city");
        String district = params.get("district");
        String street = params.get("street");
        String postalCode = params.get("postalCode");
        String addressTypeStr = params.get("addressType");

        if (country == null || addressTypeStr == null) {
            return Result.error("缺少必要参数");
        }

        AddressType addressType;
        try {
            addressType = AddressType.valueOf(addressTypeStr);
        } catch (IllegalArgumentException e) {
            return Result.error("无效的地址类型: " + addressTypeStr);
        }

        UserAddress userAddress = new UserAddress();
        userAddress.setUser(userService.findByUserId(userId));
        userAddress.setCountry(country);
        userAddress.setState(state);
        userAddress.setCity(city);
        userAddress.setDistrict(district);
        userAddress.setStreet(street);
        userAddress.setPostalCode(postalCode);
        userAddress.setAddressType(addressType);

        try {
            userService.addAddress(userAddress);
            return Result.success();
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }


    @DeleteMapping("/deleteAddress")
    public Result deleteAddress(@RequestBody Map<String, String> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        String addressTypeStr = params.get("addressType");

        if (addressTypeStr == null) {
            return Result.error("缺少必要参数");
        }

        AddressType addressType;
        try {
            addressType = AddressType.valueOf(addressTypeStr);
        } catch (IllegalArgumentException e) {
            return Result.error("无效的地址类型: " + addressTypeStr);
        }

        userService.deleteAddress(userId, addressType);
        return Result.success();
    }

    @PostMapping("/updateAddress")
    public Result updateAddress(@RequestBody Map<String, String> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        String addressTypeStr = params.get("addressType");

        if (addressTypeStr == null) {
            return Result.error("缺少必要参数");
        }

        AddressType addressType;
        try {
            addressType = AddressType.valueOf(addressTypeStr);
        } catch (IllegalArgumentException e) {
            return Result.error("无效的地址类型: " + addressTypeStr);
        }

        UserAddress userAddress = new UserAddress();
        userAddress.setCountry(params.get("country"));
        userAddress.setState(params.get("state"));
        userAddress.setCity(params.get("city"));
        userAddress.setDistrict(params.get("district"));
        userAddress.setStreet(params.get("street"));
        userAddress.setPostalCode(params.get("postalCode"));
        userAddress.setAddressType(addressType);

        userService.updateAddress(userAddress, userId, addressType);
        return Result.success();
    }


    @PostMapping("/addAppeal")
    public Result addAppeal(@RequestBody Map<String, String> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer appellantId = (Integer) claims.get("id");
        String reason = params.get("reason");

        if (reason == null) {
            return Result.error("缺少申诉理由");
        }

        Appeal appeal = new Appeal();
        appeal.setAppellantId(appellantId);
        appeal.setReason(reason);

        userService.addAppeal(appeal);
        return Result.success();
    }

    @PostMapping("/addReport")
    public Result addReport(@RequestBody Map<String, Object> params) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer reporterId = (Integer) claims.get("id");
        String reason = (String) params.get("reason");
        Integer commentId = (Integer) params.get("commentId");
        Integer matchId = (Integer) params.get("matchId");

        if (reason == null) {
            return Result.error("缺少理由");
        }
        if (commentId != null && matchId != null) {
            return Result.error("参数重复");
        }
        if (commentId == null && matchId == null) {
            return Result.error("参数缺少");
        }

        Integer reportedId = null;
        if (commentId != null) {
            ReservationComment reservationComment = reservationCommentMapper.getCoachCommentById(commentId);
            if (reservationComment != null) {
                reportedId = reservationComment.getCoachId();
            } else {
                return Result.error("未找到对应的预约评论");
            }
        } else if (matchId != null) {
            SuccessfulMatch successfulMatch = successfulMatchMapper.findById(matchId);
            if (successfulMatch != null) {
                if (successfulMatch.getUserId1().equals(reporterId)) {
                    reportedId = successfulMatch.getUserId2();
                } else if (successfulMatch.getUserId2().equals(reporterId)) {
                    reportedId = successfulMatch.getUserId1();
                } else {
                    return Result.error("当前用户不是该比赛的参与者");
                }
            } else {
                return Result.error("未找到对应的比赛记录");
            }
        }

        Report report = new Report();
        report.setReporterId(reporterId);
        report.setReason(reason);
        report.setReportedId(reportedId);
        report.setMatchId(matchId);
        report.setCommentId(commentId);

        if (!report.isValid()) {
            return Result.error("matchId 和 commentId 有且只能有一个");
        }

        userService.addReport(report);
        return Result.success();

    }
}
