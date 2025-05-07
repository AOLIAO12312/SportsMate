package com.sportsmate.service;

import com.sportsmate.pojo.User;

public interface UserService {
    //注册用户
    void register(String username, String passwd);

    //根据用户名查询用户
    User findByUserName(String username);

    User findByUserId(Integer id);
}
