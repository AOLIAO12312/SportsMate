package com.sportsmate.service;

import com.sportsmate.dto.VenueDTO;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.Venue;

public interface VenueService {

    void add(Venue venue);

    Venue findByName(String name);

    void update(Venue venue);

    PageBean<VenueDTO> list(Integer pageNum, Integer pageSize);
}
