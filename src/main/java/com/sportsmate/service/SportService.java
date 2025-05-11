package com.sportsmate.service;

import com.sportsmate.pojo.Sport;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public interface SportService {

    Sport findByName(@Pattern(regexp = "^\\S{1,16}$") String sportName);

    void add(@Pattern(regexp = "^\\S{1,16}$") String sportName, String description);

    List<String> getAllSportNames();

    String getDescription(String sportName);

    void update(Sport sport);
}
