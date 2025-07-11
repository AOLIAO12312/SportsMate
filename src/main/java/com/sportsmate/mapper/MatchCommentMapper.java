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


import com.sportsmate.pojo.MatchComment;
import org.apache.ibatis.annotations.*;


@Mapper
public interface MatchCommentMapper {

    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Insert("INSERT INTO comment(user_id, match_id, opponent_or_coach_rating, venue_rating, created_at) " +
            "VALUES(#{userId}, #{matchId}, #{opponentRating}, #{venueRating}, #{createdAt})")
    void addComment(MatchComment comment);


    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Delete("delete from comment where id = #{id}")
    void deleteComment(Integer id);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Update("update comment set user_id = #{userId}, match_id = #{matchId}, opponent_or_coach_rating = #{opponentRating}, " +
            "venue_rating = #{venueRating}, created_at = #{createdAt} where id = #{id}")
    void updateComment(MatchComment comment);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("select * from comment where id = #{id}")
    MatchComment getCommentById(Integer id);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("select * from comment where user_id = #{userId} and match_id = #{matchId}")
    MatchComment findByMatchAndUserId(Integer userId, Integer matchId);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment WHERE user_id = #{userId}")
    List<MatchComment> getCommentsByUserId(Integer userId);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("<script>" +
            "SELECT * FROM comment WHERE user_id IN " +
            "<foreach item='item' index='index' collection='userIds' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    List<MatchComment> getCommentsByUserIds(List<Integer> userIds);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment WHERE user_id = (SELECT id FROM users WHERE username = #{username1})")
    List<MatchComment> getCommentsByUsername1(String username1);

    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment WHERE match_id = #{match_id}")
    List<MatchComment> getCommentsByMatchId(Integer match_id);

    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment WHERE user_id IN (SELECT id FROM users WHERE username IN (#{username1}, #{username2})) AND match_id IN (SELECT match_id FROM comment WHERE user_id = (SELECT id FROM users WHERE username = #{username1}) INTERSECT SELECT match_id FROM comment WHERE user_id = (SELECT id FROM users WHERE username = #{username2}))")
    List<MatchComment> getCommentsByUsername1AndUsername2(String username1, String username2);
    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment")
    List<MatchComment> getAllComments();

    @Select("SELECT * FROM comment WHERE match_id IN (SELECT id FROM successful_matches WHERE venue_id = #{venueId})")
    List<MatchComment> getCommentsByVenueId(Integer venueId);

    @Results({
            @Result(property = "opponentRating", column = "opponent_or_coach_rating"),
            @Result(property = "venueRating", column = "venue_rating")
    })
    @Select("SELECT * FROM comment WHERE user_id = (SELECT id FROM users WHERE username = #{username1}) AND match_id = #{matchId}")
    List<MatchComment> getCommentsByUsername1AndMatchId(String username1, Integer matchId);}
