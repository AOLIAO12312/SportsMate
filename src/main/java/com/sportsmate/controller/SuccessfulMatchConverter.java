package com.sportsmate.controller;

import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.Sport;
import com.sportsmate.pojo.SuccessfulMatch;
import com.sportsmate.pojo.User;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class SuccessfulMatchConverter {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SportMapper sportMapper;

    public SuccessfulMatchDTO toDTO(SuccessfulMatch successfulMatch){
        SuccessfulMatchDTO dto = new SuccessfulMatchDTO();
        dto.setId(successfulMatch.getId());

        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        Integer opponentId = (Objects.equals(successfulMatch.getUserId1(), loginUserId))? successfulMatch.getUserId2() : successfulMatch.getUserId1();
        User opponent = userMapper.findByUserId(opponentId);
        dto.setOpponentName(opponent.getUsername());
        dto.setOpponentGender(opponent.getGender());
        dto.setOpponentPhone(opponent.getPhone());
        dto.setOpponentRankScore(opponent.getRankScore());
        dto.setStatus(successfulMatch.getStatus());
        dto.setStartTime(successfulMatch.getStartTime());
        dto.setEndTime(successfulMatch.getEndTime());
        dto.setCreatedAt(successfulMatch.getCreatedAt());

        Sport sport = sportMapper.findBySportId(successfulMatch.getSportId());
        dto.setSportName(sport.getSportName());
        return dto;
    }
}
