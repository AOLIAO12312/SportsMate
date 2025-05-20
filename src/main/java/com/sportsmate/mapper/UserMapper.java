package com.sportsmate.mapper;

import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import com.sportsmate.pojo.UserAddress;
import com.sportsmate.pojo.AddressType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into users(username,password,user_type,created_at,updated_at)" +
            "values (#{username},#{password},#{userType},now(),now())")
    void add(String username, String password, UserType userType);

    @Select("select * from users where username=#{username}")
    User findByUserName(String username);

    @Select("select * from users where id=#{id}")
    User findByUserId(Integer id);

    @Update("update users set gender=#{gender},phone=#{phone} where id=#{id} ")
    void update(User user);

    @Update("update users set password=#{md5String} where id=#{id}")
    void updatePwd(String md5String, Integer id);


    @Insert("INSERT INTO user_addresses (user_id, country, state, city, district, street, postal_code, address_type, created_at, updated_at) " +
                "VALUES (#{user.id}, #{country}, #{state}, #{city}, #{district}, #{street}, #{postalCode}, #{addressType}, NOW(), NOW())")
    void addAddress(UserAddress userAddress);

    @Select("SELECT id FROM user_addresses WHERE user_id = #{userId} AND address_type = #{addressType}")
    Integer findAddressIdByUserIdAndType(Integer userId, AddressType addressType);

    @Delete("DELETE FROM user_addresses WHERE id = #{addressId}")
    void deleteAddress(Integer addressId);

    @Update("UPDATE user_addresses SET country = #{country}, state = #{state}, city = #{city}, district = #{district}, street = #{street}, postal_code = #{postalCode}, address_type = #{addressType}, updated_at = NOW() WHERE id = #{id}")
    void updateAddress(UserAddress userAddress);

    @Insert("INSERT INTO reports (reporter_id, reported_id, reason, match_id, comment_id, status) VALUES (#{reporterId}, #{reportedId}, #{reportReason}, #{matchId}, #{commentId}, '未处理')")
    void addReport(Report report);

    @Insert("INSERT INTO appeals (appellant_id, reason, status) VALUES (#{appellantId}, #{reason}, '未处理')")
    void addAppeal(Appeal appeal);

    @Select("SELECT COUNT(*) FROM user_addresses WHERE user_id = #{userId} AND address_type = #{addressType}")
    int countAddressesByUserIdAndType(Integer userId, AddressType addressType);

    // 封禁/解封用户（根据需求可能需要UserMapper处理）
    @Update("UPDATE users SET status = '封禁' WHERE id = #{userId}")
    void banUser(Integer userId);

    @Update("UPDATE users SET status = '正常' WHERE id = #{userId}")
    void unbanUser(Integer userId);

    @Select("SELECT * FROM users WHERE username = #{username}")
    List<User> getUsersByUsername(String username);

    @Select("SELECT * FROM users WHERE user_type = #{userType}")
    List<User> getUsersByUserType(UserType userType);

    @Select("SELECT * FROM users WHERE status = #{userStatus}")
    List<User> getUsersByUserStatus(UserStatus userStatus);
}
