package com.sportsmate.service.impl;

import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.User;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.Md5Util;
import com.sportsmate.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String passwd,String userType) {
        //加密处理
        String md5String = Md5Util.getMD5String(passwd);
        //添加
        userMapper.add(username,md5String,userType);
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

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),id);
    }

}
