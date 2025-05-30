package com.sportsmate.service;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserAddress;
import com.sportsmate.pojo.AddressType;
import com.sportsmate.pojo.UserType;

import java.util.List;

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

    //添加地址
    void addAddress(UserAddress userAddress);

    //删除地址
    void deleteAddress(Integer userId, AddressType addressType);

    //更新地址
    void updateAddress(UserAddress userAddress, Integer userId, AddressType addressType);

    //添加举报
    void addReport(Report report);

    //添加申述
    void addAppeal(Appeal appeal);

    List<UserAddress> getAddress(Integer userId);
}
