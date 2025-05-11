package com.sportsmate.service.impl;

import com.sportsmate.mapper.CoachProfileMapper;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CoachProfileServiceImpl implements CoachProfileService {
    @Autowired
    private CoachProfileMapper coachProfileMapper;

    @Override
    public CoachProfile findByUserId(Integer id) {
        return coachProfileMapper.findByUserId(id);
    }

    @Override
    public void updateProfile(CoachProfile profile) {
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer id = (Integer) claims.get("id");
        profile.setUserId(id);
        coachProfileMapper.updateProfile(profile);
    }

    @Override
    public void add(Integer registerUserId) {
        coachProfileMapper.add(registerUserId);
    }
}
