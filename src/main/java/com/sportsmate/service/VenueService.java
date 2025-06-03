package com.sportsmate.service;

import com.sportsmate.dto.VenueDTO;
import com.sportsmate.dto.VenueSportDTO;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Venue;
import com.sportsmate.pojo.VenueSport;

import java.util.List;
import java.util.Set;

public interface VenueService {

    void add(Venue venue);

    Venue findByName(String name);

    void update(Venue venue);

    PageBean<VenueDTO> list(Integer pageNum, Integer pageSize);

    void batchInsertVenueSports(List<VenueSportDTO> venueSports);

    Venue findById(Integer id);
}
