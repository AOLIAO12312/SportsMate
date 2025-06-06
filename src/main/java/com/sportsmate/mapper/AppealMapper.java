package com.sportsmate.mapper;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppealMapper {

    // Appeal相关方法


    @Select("SELECT * FROM appeals WHERE id = #{id}")
    Appeal getAppealById(Integer id);

    @Select("SELECT * FROM appeals WHERE appellant_id = #{appellantId}")
    Appeal getAppealByAppellantId(Integer appellantId);

    @Update("UPDATE appeals SET reason = #{reason}, status = #{status}, reply_message = #{replyMessage} WHERE id = #{id}")
    void updateAppeal(Appeal appeal);

    @Update("UPDATE appeals SET status = #{status}, reply_message = #{replyMessage} WHERE id = #{id}")
    void updateAppealStatus(@Param("id") Integer id, @Param("status") HandleStatus status, @Param("replyMessage") String replyMessage);
    @Select("SELECT * FROM appeals ORDER BY created_at DESC")
    List<Appeal> getAllAppeals();

    @Select("SELECT * FROM appeals WHERE appellant_id = (SELECT id FROM users WHERE username = #{appellantname}) ORDER BY created_at DESC")
    List<Appeal> getAppealsByAppellantname(String appellantname);

    @Select("SELECT * FROM appeals WHERE status = #{status} ORDER BY created_at DESC")
    List<Appeal> getAppealsByStatus(HandleStatus status);

    @Select("SELECT * FROM appeals WHERE appellant_id = (SELECT id FROM users WHERE username = #{appellantname}) AND status = #{status} ORDER BY created_at DESC")
    List<Appeal> getAppealsByAppellantnameAndStatus(String appellantname, HandleStatus status);




}