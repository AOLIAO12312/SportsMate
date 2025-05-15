package com.sportsmate.service;

import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.PageBean;

public interface CoachProfileService {
    CoachProfile findByUserId(Integer id);

    void updateProfile(CoachProfile profile);

    void add(Integer registerUserId);

    PageBean<CoachProfileDTO> listCoach(Integer pageNum, Integer pageSize,String sportName);
}
