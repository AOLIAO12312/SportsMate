package com.sportsmate.mapper;

import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.SuccessfulMatchStatus;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SuccessfulMatchMapper {

    @Insert("INSERT INTO successful_matches(" +
            "match_request_id1, match_request_id2, user_id1, user_id2, sport_id, venue_id, start_time, end_time, status, created_at" +
            ") VALUES (" +
            "#{matchRequestId1}, #{matchRequestId2}, #{userId1}, #{userId2}, #{sportId}, #{venueId}, " +
            "#{startTime}, #{endTime}, #{status}, #{createdAt}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addSuccessfulMatch(SuccessfulMatch successfulMatch);

    @Select("SELECT * FROM successful_matches " +
            "WHERE user_id1 = #{loginUserId} OR user_id2 = #{loginUserId}")
    List<SuccessfulMatch> list(Integer loginUserId);

    @Select("SELECT * FROM successful_matches " +
            "WHERE (user_id1 = #{loginUserId} OR user_id2 = #{loginUserId}) " +
            "AND status = '待完成' ")
    List<SuccessfulMatch> findActiveMatchByUserId(Integer loginUserId);

    @Select("select * from successful_matches where (match_request_id1 = #{requestId} or match_request_id2 = #{requestId}) ")
    SuccessfulMatch findByRequestId(Integer requestId);

    @Select("select * from successful_matches where id=#{successfulMatchId}")
    SuccessfulMatch findById(Integer successfulMatchId);

    @Update("update successful_matches set status='已取消' where id=#{id}")
    void cancel(Integer id);

    @Update("UPDATE successful_matches SET status = #{status} WHERE id = #{id}")
    void updateStatus(Integer id, SuccessfulMatchStatus status);
}
