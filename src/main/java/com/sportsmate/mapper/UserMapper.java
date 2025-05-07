package com.sportsmate.mapper;

import com.sportsmate.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into users(username,password,created_at,updated_at)" +
            "values (#{username},#{password},now(),now())")
    void add(String username, String password);

    @Select("select * from users where username=#{username}")
    User findByUserName(String username);

    @Select("select * from users where id=#{id}")
    User findByUserId(Integer id);
}
