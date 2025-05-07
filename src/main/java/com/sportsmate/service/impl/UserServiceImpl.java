package com.sportsmate.service.impl;

import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.User;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String passwd) {
        //加密处理
        String md5String = Md5Util.getMD5String(passwd);
        //添加
        userMapper.add(username,md5String);
    }

    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }

    @Override
    public User findByUserId(Integer id) {
        return userMapper.findByUserId(id);
    }
}
