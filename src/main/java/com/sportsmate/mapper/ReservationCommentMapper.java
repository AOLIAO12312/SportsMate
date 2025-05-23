package com.sportsmate.mapper;

import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.UserType;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationCommentMapper {

    @Insert("insert into coach_comment(user_id, coach_id, coach_reservation_id, coach_rating, coach_comment, created_at)"+
            "values(#{userId}, #{coachId}, #{coachReservationId}, #{coachRating}, #{coachComment}, #{createdAt})")
    void addCoachComment(ReservationComment coachComment);

    @Delete("delete from coach_comment where id = #{id}")
    void deleteCoachComment(Integer id);

    @Update("update coach_comment set user_id = #{userId}, coach_id = #{coachId}, coach_reservation_id = #{coachReservationId}, " +
            "coach_rating = #{coachRating}, coach_comment = #{coachComment}, created_at = #{createdAt} where id = #{id}")
    void updateCoachComment(ReservationComment coachComment);

    @Select("select * from coach_comment where id = #{id}")
    ReservationComment getCoachCommentById(Integer id);

    @Select("select * from coach_comment where user_id = #{userId} and coach_reservation_id = #{reservationId}")
    ReservationComment findByUserAndCoachId(Integer userId, Integer reservationId);

    // ReservationCommentMapper
    @Select("SELECT * FROM coach_comment WHERE user_id = (SELECT id FROM users WHERE username = #{username})")
    List<ReservationComment> getCommentsByUsername(String username);

    @Select("SELECT * FROM coach_comment WHERE coach_id = (SELECT id FROM users WHERE username = #{coachName})")
    List<ReservationComment> getCommentsByCoachname(String coachName);

    @Select("SELECT * FROM coach_comment WHERE user_id = (SELECT id FROM users WHERE username = #{username}) AND coach_id = (SELECT id FROM users WHERE username = #{coachname})")
    List<ReservationComment> getCommentsByUsernameAndCoachname(String username, String coachName);

    @Select("SELECT * FROM coach_comment")
    List<ReservationComment> getAllComments();

    @Select("SELECT * FROM coach_comment WHERE coach_reservation_id = #{coachReservationId}")
    ReservationComment findByCoachReservationId(Integer coachReservationId);
    @Select("SELECT * FROM coach_comment WHERE user_id = #{userId}")
    List<ReservationComment> getCommentsByUserId(Integer userId);



}