package com.sportsmate.converter;

import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Sport;

public class CoachProfileConverter {

    public static CoachProfile toEntity(CoachProfileDTO dto) {
        CoachProfile profile = new CoachProfile();
        profile.setUserId(dto.getUserId());
        profile.setRealName(dto.getRealName());
        profile.setProfileDescription(dto.getProfileDescription());


        profile.setCoachedSports(sport);


        profile.setRating(dto.getRating());
        profile.setIsActive(dto.getIsActive());
        return profile;
    }

    public static CoachProfileDTO toDTO(CoachProfile entity) {
        CoachProfileDTO dto = new CoachProfileDTO();
        dto.setUserId(entity.getUserId());
        dto.setRealName(entity.getRealName());
        dto.setProfileDescription(entity.getProfileDescription());


        dto.setCoachedSportName(entity.getCoachedSports().getName()); // 你需要确保 Sport 有 getName()


        dto.setRating(entity.getRating());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}