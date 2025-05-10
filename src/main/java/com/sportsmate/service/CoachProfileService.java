package com.sportsmate.service;

import com.sportsmate.pojo.CoachProfile;

public interface CoachProfileService {
    CoachProfile findByUserId(Integer id);

    void updateProfile(CoachProfile profile);

    void add(Integer registerUserId);
}
