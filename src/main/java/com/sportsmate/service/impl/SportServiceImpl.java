package com.sportsmate.service.impl;

import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.Sport;
import com.sportsmate.service.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportServiceImpl implements SportService {

    @Autowired
    private SportMapper sportMapper;

    @Override
    public Sport findByName(String sportName) {
        return sportMapper.findBySportName(sportName);
    }

    @Override
    public void add(String sportName, String description) {
        sportMapper.add(sportName,description);
        return;
    }

    @Override
    public List<String> getAllSportNames() {
        return sportMapper.findAllSportNames();
    }

    @Override
    public String getDescription(String sportName) {
        return sportMapper.getDescription(sportName);
    }

    @Override
    public void update(Sport sport) {
        sportMapper.update(sport);
    }
}
