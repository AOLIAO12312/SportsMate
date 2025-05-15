package com.sportsmate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsmate.converter.CoachProfileConverter;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.VenueDTO;
import com.sportsmate.mapper.CoachProfileMapper;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.CoachProfile;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Sport;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.service.SportService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CoachProfileServiceImpl implements CoachProfileService {
    @Autowired
    private CoachProfileMapper coachProfileMapper;

    @Autowired
    private CoachProfileConverter coachProfileConverter;

    @Autowired
    private SportMapper sportMapper;

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

    @Override
    public PageBean<CoachProfileDTO> listCoach(Integer pageNum, Integer pageSize,String sportName) {

        PageBean<CoachProfileDTO> pb = new PageBean<>();

        PageHelper.startPage(pageNum, pageSize);
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        Sport sport = sportMapper.findBySportName(sportName);
        if(sport == null){
            return null;
        }
        Integer sportId = sport.getId();
        // 这一步会被 PageHelper 自动分页
        List<CoachProfile> as = coachProfileMapper.list(sportId);

        // PageHelper 会返回 Page 类型（as 被代理）
        Page<CoachProfile> page = (Page<CoachProfile>) as;

        List<CoachProfileDTO> dtos = new ArrayList<>();
        for (CoachProfile coachProfile : as) {
            CoachProfileDTO dto = coachProfileConverter.toDTO(coachProfile);
            dtos.add(dto);
        }

        pb.setTotal(page.getTotal());
        pb.setItems(dtos);
        return pb;
    }
}
