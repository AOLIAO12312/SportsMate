package com.sportsmate.mapper;

import com.sportsmate.pojo.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment(user_id,match_id,opponent_or_coach_rating,venue_rating,created_at)"+
    "values(#{userId},#{matchId},#{opponentOrCoachRating},#{venueRating},#{createdAt})")
    void addComment(Comment comment);

    @Delete("delete from comment where id = #{id}")
    void deleteComment(Integer id);

    @Update("update comment set user_id = #{userId}, match_id = #{matchId}, opponent_or_coach_rating = #{opponentOrCoachRating}, " +
            "venue_rating = #{venueRating}, created_at = #{createdAt} where id = #{id}")
    void updateComment(Comment comment);

    @Select("select * from comment where id = #{id}")
    Comment getCommentById(Integer id);

    @Select("select * from comment where user_id = #{userId} and match_id = #{matchId}")
    Comment findByMatchAndUserId(Integer userId, Integer matchId);

    @Select("SELECT * FROM comment WHERE user_id = #{userId}")
    List<Comment> getCommentsByUserId(Integer userId);

    @Select("<script>" +
            "SELECT * FROM comment WHERE user_id IN " +
            "<foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<Comment> getCommentsByUserIds(List<Integer> userIds);
}
