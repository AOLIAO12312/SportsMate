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

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationCommentMapper {

    @Insert("insert into coach_comment(user_id, coach_id, coach_reservation_id, coach_rating, coach_comment, venue_rating,created_at)"+
            "values(#{userId}, #{coachId}, #{coachReservationId}, #{coachRating}, #{coachComment}, #{venueRating} ,#{createdAt})")
    void addCoachComment(ReservationComment coachComment);

    @Delete("delete from coach_comment where id = #{id}")
    void deleteCoachComment(Integer id);

    @Update("update coach_comment set user_id = #{userId}, coach_id = #{coachId}, coach_reservation_id = #{coachReservationId}, " +
            "coach_rating = #{coachRating}, coach_comment = #{coachComment}, created_at = #{createdAt},venue_rating = #{venueRating} where id = #{id}")
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

    @Select("select * from coach_comment where coach_id = #{coachId}")
    List<ReservationComment> findCommentByCoachId(Integer coachId);

    @Insert("INSERT INTO forbidden_word_daily (stat_date, count)\n" +
            "VALUES (CURDATE(), 1)\n" +
            "ON DUPLICATE KEY UPDATE count = count + 1;\n")
    void updateForbiddenCount();

    @Select("select count from forbidden_word_daily where stat_date=#{date}")
    int getForbiddenCount(LocalDate date);

    @Select("SELECT * FROM coach_comment WHERE coach_reservation_id IN (SELECT id FROM coach_reservation WHERE venue_id = #{venueId})")
    List<ReservationComment> getCommentsByVenueId(Integer venueId);

    @Select("SELECT * FROM coach_comment WHERE user_id = #{userId} AND coach_reservation_id = #{reservationId}")
    ReservationComment findByUserAndReservationId(Integer userId, Integer reservationId);
}