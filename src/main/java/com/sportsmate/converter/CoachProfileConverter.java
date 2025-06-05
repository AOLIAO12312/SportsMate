package com.sportsmate.converter;

import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.Sport;
import com.sportsmate.pojo.User;
import com.sportsmate.pojo.UserAddress;
import com.sportsmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoachProfileConverter {

    @Autowired
    private SportMapper sportMapper;

    @Autowired
    private UserService userService;

    public CoachProfile toEntity(CoachProfileDTO dto) {
        CoachProfile profile = new CoachProfile();
        profile.setUserId(dto.getUserId());
        profile.setRealName(dto.getRealName());
        profile.setProfileDescription(dto.getProfileDescription());

        //查询运动并赋值
        Sport sport = sportMapper.findBySportName(dto.getCoachedSportName());
        if (sport == null) {
            throw new IllegalArgumentException("Sport not found for name: " + dto.getCoachedSportName());
        }

        profile.setCoachedSports(sport.getId());


        profile.setRating(dto.getRating());
        profile.setIsActive(dto.getIsActive());
        return profile;
    }

    public CoachProfileDTO toDTO(CoachProfile entity) {
        CoachProfileDTO dto = new CoachProfileDTO();
        dto.setUserId(entity.getUserId());
        dto.setRealName(entity.getRealName());
        dto.setProfileDescription(entity.getProfileDescription());

        if(entity.getCoachedSports() != null){
            Sport sport = sportMapper.findBySportId(entity.getCoachedSports());
            if (sport == null) {
                throw new IllegalArgumentException("Sport not found for id: " + entity.getCoachedSports());
            }
            dto.setCoachedSportName(sport.getSportName()); // 你需要确保 Sport 有 getName()
        }else {
            dto.setCoachedSportName("未设置");
        }
        UserAddress userAddress = userService.getDefaultAddressById(entity.getUserId());
        if(userAddress != null){
            String country = userAddress.getCountry();
            String state = userAddress.getState();
            String city = userAddress.getCity();
            String district = userAddress.getDistrict();
            String street = userAddress.getStreet();
            dto.setAddress(country+state+city+district+street);
        }

        User coach = userService.findByUserId(entity.getUserId());
        if(coach == null){
            return null;
        }
        dto.setPhone(coach.getPhone());

        dto.setRating(entity.getRating());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}