package com.sportsmate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsmate.converter.CoachProfileConverter;
import com.sportsmate.dto.CoachProfileDTO;
import com.sportsmate.mapper.CoachProfileMapper;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.*;
import com.sportsmate.service.CoachProfileService;
import com.sportsmate.service.SportService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;


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
    public PageBean<CoachProfileDTO> listCoach(Integer pageNum, Integer pageSize, String sportName) {
        PageBean<CoachProfileDTO> pb = new PageBean<>();

        Sport sport = sportMapper.findBySportName(sportName);
        if (sport == null) {
            return null;
        }

        Integer sportId = sport.getId();

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);

        // 查询列表（PageHelper 会自动拦截这个查询并分页）
        List<CoachProfile> as = coachProfileMapper.list(sportId);

        // 包装成 PageInfo 获取 total 总数
        PageInfo<CoachProfile> pageInfo = new PageInfo<>(as);

        // 转换成 DTO
        List<CoachProfileDTO> dtos = new ArrayList<>();
        for (CoachProfile coachProfile : as) {
            CoachProfileDTO dto = coachProfileConverter.toDTO(coachProfile);
            dtos.add(dto);
        }

        pb.setTotal(pageInfo.getTotal()); // 正确获取分页总条数
        pb.setItems(dtos);

        return pb;
    }

}
