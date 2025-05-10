package com.sportsmate.service;

import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;

public interface UserService {
    //注册用户
    void register(String username, String passwd, UserType userType);

    //根据用户名查询用户
    User findByUserName(String username);

    User findByUserId(Integer id);

    //更新用户基本信息
    void update(User user);

    //更新用户密码
    void updatePwd(String newPwd);
}
