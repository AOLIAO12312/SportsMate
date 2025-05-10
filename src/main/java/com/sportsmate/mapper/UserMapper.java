package com.sportsmate.mapper;

import com.sportsmate.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
    @Insert("insert into users(username,password,user_type,created_at,updated_at)" +
            "values (#{username},#{password},#{userType},now(),now())")
    void add(String username, String password,String userType);

    @Select("select * from users where username=#{username}")
    User findByUserName(String username);

    @Select("select * from users where id=#{id}")
    User findByUserId(Integer id);

    @Update("update users set gender=#{gender},phone=#{phone} where id=#{id} ")
    void update(User user);

    @Update("update users set password=#{md5String} where id=#{id}")
    void updatePwd(String md5String, Integer id);
}
