package com.sportsmate.mapper;

import com.sportsmate.pojo.Appeal;
import com.sportsmate.pojo.HandleStatus;
import com.sportsmate.pojo.Report;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    // Report相关方法
    @Select("SELECT * FROM reports")
    List<Report> getAllReports();

    @Select("SELECT * FROM reports WHERE id = #{id}")
    Report getReportById(Integer id);

    @Update("UPDATE reports SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    void updateReportStatus(@Param("id") Integer id, @Param("status") HandleStatus status);

    // Appeal相关方法
    @Select("SELECT * FROM appeals")
    List<Appeal> getAllAppeals();

    @Select("SELECT * FROM appeals WHERE id = #{id}")
    Appeal getAppealById(Integer id);

    @Update("UPDATE appeals SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    void updateAppealStatus(@Param("id") Integer id, @Param("status") HandleStatus status);

    // 封禁/解封用户（根据需求可能需要UserMapper处理）
    @Update("UPDATE users SET status = '封禁' WHERE id = #{userId}")
    void banUser(Integer userId);

    @Update("UPDATE users SET status = '正常' WHERE id = #{userId}")
    void unbanUser(Integer userId);
}