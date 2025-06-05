package com.sportsmate.mapper;

import com.sportsmate.pojo.Gender;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.MatchRequestStatus;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MatchRequestMapper {

    @Insert("insert into match_requests(user_id,sport_id,expected_opponent_gender,venue_id,start_time,end_time,status,created_at) "+
    "values (#{userId},#{sportId},#{expectedOpponentGender},#{venueId},#{startTime},#{endTime},#{status},#{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addRequest(MatchRequest matchRequest);

    @Select("SELECT * FROM match_requests WHERE user_id = #{loginUserId} AND status = '待匹配' LIMIT 1")
    MatchRequest findActiveRequestByUserId(Integer loginUserId);

    @Select("select * from match_requests where user_id=#{loginUserId}")
    List<MatchRequest> list(Integer loginUserId);

    @Select("select * from match_requests where id=#{requestId}")
    MatchRequest findById(Integer requestId);

    @Update("update match_requests set status='已取消',remark=#{remark} where id=#{requestId}")
    void cancel(Integer requestId, String remark);

    @Select("""
    SELECT * FROM match_requests
    WHERE sport_id = #{sportId}
      AND user_id != #{loginUserId}
      AND status = '待匹配'
      AND (
        (#{startTime} <= end_time AND #{endTime} >= start_time)
      )
      AND venue_id=#{venueId}
    """)
    List<MatchRequest> findPotentialMatches(@NotNull Integer sportId, Gender expectedOpponentGender, LocalDateTime startTime, LocalDateTime endTime,Integer venueId, Integer loginUserId);

    @Update("update match_requests set status=#{status} where id=#{id}")
    void updateStatus(MatchRequest candidate);

    // MyBatis 注解写法（若你用的是注解方式）
    @Select("<script>" +
            "SELECT * FROM match_requests " +
            "WHERE user_id = #{userId} " +
            "<if test='status != null'>" +
            "AND status = #{status}" +
            "</if>" +
            "ORDER BY created_at DESC" +
            "</script>")
    List<MatchRequest> listByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") MatchRequestStatus status);

}
