package com.sportsmate.converter;

import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.dto.VenueDTO;
import com.sportsmate.mapper.MatchRequestMapper;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.*;
import com.sportsmate.service.VenueService;
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

    @Autowired
    private MatchRequestMapper matchRequestMapper;

    @Autowired
    private VenueService venueService;

    public SuccessfulMatchDTO toDTO(SuccessfulMatch successfulMatch){
        SuccessfulMatchDTO dto = new SuccessfulMatchDTO();
        dto.setId(successfulMatch.getId());

        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");
        Integer opponentId;
        Integer opponentRequestId;

        Integer myRequestId;
        if(Objects.equals(successfulMatch.getUserId1(), loginUserId)){
            opponentId = successfulMatch.getUserId2();
            opponentRequestId = successfulMatch.getMatchRequestId2();
            myRequestId = successfulMatch.getMatchRequestId1();
        }else {
            opponentId = successfulMatch.getUserId1();
            opponentRequestId = successfulMatch.getMatchRequestId1();
            myRequestId = successfulMatch.getMatchRequestId2();
        }

        User opponent = userMapper.findByUserId(opponentId);
        MatchRequest matchRequest = matchRequestMapper.findById(opponentRequestId);
        MatchRequest myMatchRequest = matchRequestMapper.findById(myRequestId);

        dto.setOpponentName(opponent.getUsername());
        dto.setOpponentGender(opponent.getGender());
        dto.setOpponentPhone(opponent.getPhone());
        dto.setOpponentRankScore(opponent.getRankScore());
        dto.setStatus(successfulMatch.getStatus());
        dto.setStartTime(successfulMatch.getStartTime());
        dto.setEndTime(successfulMatch.getEndTime());
        dto.setCreatedAt(successfulMatch.getCreatedAt());
        dto.setRemark(myMatchRequest.getRemark());

        Venue venue =  venueService.findById(successfulMatch.getVenueId());
        if(venue != null){
            VenueDTO venueDTO = new VenueDTO();
            venueDTO.setName(venue.getName());
            venueDTO.setOpeningTime(venue.getOpeningTime());
            venueDTO.setClosingTime(venue.getClosingTime());
            venueDTO.setFullAddress(venue.getFullAddress());
            dto.setVenueDTO(venueDTO);
        }

        Sport sport = sportMapper.findBySportId(successfulMatch.getSportId());
        dto.setSportName(sport.getSportName());
        return dto;
    }
}
