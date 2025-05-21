package com.sportsmate.mapper;

import com.sportsmate.pojo.MatchComment;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.UserStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchCommentMapper {

    @Insert("insert into comment(user_id,match_id,opponent_or_coach_rating,venue_rating,created_at)"+
    "values(#{userId},#{matchId},#{opponentOrCoachRating},#{venueRating},#{createdAt})")
    void addComment(MatchComment comment);

    @Delete("delete from comment where id = #{id}")
    void deleteComment(Integer id);

    @Update("update comment set user_id = #{userId}, match_id = #{matchId}, opponent_or_coach_rating = #{opponentOrCoachRating}, " +
            "venue_rating = #{venueRating}, created_at = #{createdAt} where id = #{id}")
    void updateComment(MatchComment comment);

    @Select("select * from comment where id = #{id}")
    MatchComment getCommentById(Integer id);

    @Select("select * from comment where user_id = #{userId} and match_id = #{matchId}")
    MatchComment findByMatchAndUserId(Integer userId, Integer matchId);

    @Select("SELECT * FROM comment WHERE user_id = #{userId}")
    List<MatchComment> getCommentsByUserId(Integer userId);

    @Select("<script>" +
            "SELECT * FROM comment WHERE user_id IN " +
            "<foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<MatchComment> getCommentsByUserIds(List<Integer> userIds);

    @Select("<script>" +
            "SELECT * FROM comment " +
            "<where>" +
            "  user_id IN (" +
            "    SELECT id FROM users " +
            "    <where>" +
            "      <if test='username != null'>username = #{username}</if> " +
            "      <if test='userType != null'>AND user_type = #{userType}</if> " +
            "      <if test='userStatus != null'>AND status = #{userStatus}</if> " +
            "    </where>" +
            "  )" +
            "</where>" +
            "</script>")
    List<MatchComment> getCommentsByFilters(String username, UserType userType, UserStatus userStatus);


}
