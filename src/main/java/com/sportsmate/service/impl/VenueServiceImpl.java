package com.sportsmate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsmate.dto.VenueDTO;
import com.sportsmate.mapper.VenueMapper;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Venue;
import com.sportsmate.service.VenueService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueMapper venueMapper;

    @Override
    public void add(Venue venue) {
        venueMapper.add(venue);
    }

    @Override
    public Venue findByName(String name) {
        return venueMapper.findByName(name);
    }

    @Override
    public void update(Venue venue) {
        venue.setUpdatedAt(LocalDateTime.now());
        venueMapper.update(venue);
    }

    @Override
    public PageBean<VenueDTO> list(Integer pageNum, Integer pageSize) {
        PageBean<VenueDTO> pb = new PageBean<>();

        PageHelper.startPage(pageNum, pageSize);
        List<VenueDTO> as = venueMapper.listSimple();
        Page<VenueDTO> p = (Page<VenueDTO>) as;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }



}
