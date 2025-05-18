package com.sportsmate.mapper;

import com.sportsmate.pojo.CoachComment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CoachCommentMapper {

    @Insert("insert into coach_comment(user_id, coach_id, coach_reservation_id, coach_rating, coach_comment, created_at)"+
            "values(#{userId}, #{coachId}, #{coachReservationId}, #{coachRating}, #{coachComment}, #{createdAt})")
    void addCoachComment(CoachComment coachComment);

    @Delete("delete from coach_comment where id = #{id}")
    void deleteCoachComment(Integer id);

    @Update("update coach_comment set user_id = #{userId}, coach_id = #{coachId}, coach_reservation_id = #{coachReservationId}, " +
            "coach_rating = #{coachRating}, coach_comment = #{coachComment}, created_at = #{createdAt} where id = #{id}")
    void updateCoachComment(CoachComment coachComment);

    @Select("select * from coach_comment where id = #{id}")
    CoachComment getCoachCommentById(Integer id);

    @Select("select * from coach_comment where user_id = #{userId} and coach_reservation_id = #{reservationId}")
    CoachComment findByUserAndCoachId(Integer userId, Integer reservationId);
}