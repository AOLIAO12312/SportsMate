package com.sportsmate.mapper;

import com.sportsmate.pojo.SuccessfulMatch;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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

}
