package com.sportsmate.service.impl;

import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserAddress;
import com.sportsmate.pojo.AddressType;
import com.sportsmate.service.UserService;
import com.sportsmate.utils.Md5Util;
import com.sportsmate.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String passwd, UserType userType) {
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

    @Override
    public void addAddress(UserAddress userAddress) {
        Integer userId = userAddress.getUserId();
        AddressType addressType = userAddress.getAddressType();
        int count = userMapper.countAddressesByUserIdAndType(userId, addressType);
        if (count > 0) {
            throw new IllegalArgumentException("用户已经有该类型的地址，不能重复添加");
        }
        userMapper.addAddress(userAddress);
    }

    @Override
    public void addReport(Report report) {
        report.setCreatedAt(LocalDateTime.now());
        userMapper.addReport(report);
    }

    @Override
    public void addAppeal(Appeal appeal) {
        appeal.setCreatedAt(LocalDateTime.now());
        userMapper.addAppeal(appeal);
    }

    @Override
    public List<UserAddress> getAddress(Integer userId) {
        return userMapper.getAddress(userId);
    }

    @Override
    public UserAddress getDefaultAddressById(Integer userId) {
        return userMapper.getDefaultAddressById(userId,AddressType.默认);
    }


    @Override
    public void deleteAddress(Integer userId, AddressType addressType) {
        Integer addressId = userMapper.findAddressIdByUserIdAndType(userId, addressType);
        if (addressId != null) {
            userMapper.deleteAddress(addressId);
        }
    }

    @Override
    public void updateAddress(UserAddress userAddress, Integer userId, AddressType addressType) {
        Integer addressId = userMapper.findAddressIdByUserIdAndType(userId, addressType);
        if (addressId != null) {
            userAddress.setId(addressId);
            userMapper.updateAddress(userAddress);
        }
    }

}
