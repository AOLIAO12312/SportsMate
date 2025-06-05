package com.sportsmate.converter;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.VenueDTO;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.Sport;
import com.sportsmate.pojo.Venue;
import com.sportsmate.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchRequestConverter {

    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private VenueService venueService;

    public MatchRequest toEntity(MatchRequestDTO dto){
        MatchRequest matchRequest = new MatchRequest();
        matchRequest.setId(dto.getId());

        Sport sport = sportMapper.findBySportName(dto.getSportName());
        if(sport == null){
            return null;
        }
        matchRequest.setUserId(dto.getUserId());
        matchRequest.setSportId(sport.getId());
        matchRequest.setVenueId(dto.getVenueId());
        matchRequest.setExpectedOpponentGender(dto.getExpectedOpponentGender());
        matchRequest.setStartTime(dto.getStartTime());
        matchRequest.setEndTime(dto.getEndTime());
        matchRequest.setCreatedAt(dto.getCreatedAt());
        matchRequest.setStatus(dto.getStatus());
        matchRequest.setRemark(dto.getRemark());
        return matchRequest;
    }

    public MatchRequestDTO toDTO(MatchRequest matchRequest){
        MatchRequestDTO dto = new MatchRequestDTO();
        dto.setId(matchRequest.getId());
        dto.setUserId(matchRequest.getUserId());

        Sport sport = sportMapper.findBySportId(matchRequest.getSportId());
        if (sport == null) {
            return null;
        }
        dto.setSportName(sport.getSportName());  // 假设DTO中用的是名字，实体用的是ID

        dto.setVenueId(matchRequest.getVenueId());
        Venue venue =  venueService.findById(matchRequest.getVenueId());
        if(venue != null){
            VenueDTO venueDTO = new VenueDTO();
            venueDTO.setId(matchRequest.getVenueId());
            venueDTO.setName(venue.getName());
            venueDTO.setOpeningTime(venue.getOpeningTime());
            venueDTO.setClosingTime(venue.getClosingTime());
            venueDTO.setFullAddress(venue.getFullAddress());
            dto.setVenueDTO(venueDTO);
        }
        dto.setExpectedOpponentGender(matchRequest.getExpectedOpponentGender());
        dto.setStartTime(matchRequest.getStartTime());
        dto.setEndTime(matchRequest.getEndTime());
        dto.setCreatedAt(matchRequest.getCreatedAt());
        dto.setStatus(matchRequest.getStatus());
        dto.setRemark(matchRequest.getRemark());
        return dto;
    }

}
