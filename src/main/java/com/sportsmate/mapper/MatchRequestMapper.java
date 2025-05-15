package com.sportsmate.mapper;

import com.sportsmate.pojo.Gender;
import com.sportsmate.pojo.MatchRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MatchRequestMapper {

    @Insert("insert into match_requests(user_id,sport_id,expected_opponent_gender,address_type,start_time,end_time,status,created_at) "+
    "values (#{userId},#{sportId},#{expectedOpponentGender},#{addressType},#{startTime},#{endTime},#{status},#{createdAt})")
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
    """)
    List<MatchRequest> findPotentialMatches(@NotNull Integer sportId, Gender expectedOpponentGender, LocalDateTime startTime, LocalDateTime endTime, Integer loginUserId);

    @Update("update match_requests set status=#{status} where id=#{id}")
    void updateStatus(MatchRequest candidate);
}
