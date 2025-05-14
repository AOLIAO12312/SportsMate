package com.sportsmate.converter;

import com.sportsmate.dto.VenueSportDTO;
import com.sportsmate.mapper.SportMapper;
import com.sportsmate.pojo.VenueSport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class VenueSportConverter {

    @Autowired
    private SportMapper sportMapper;

    public List<VenueSportDTO> toDTO(List<VenueSport> vs) {
        List<VenueSportDTO> dtoList = new ArrayList<>();
        for (VenueSport venueSport : vs) {
            VenueSportDTO dto = new VenueSportDTO();
            dto.setVenueId(venueSport.getVenueId());
            dto.setSportName(sportMapper.findBySportId(venueSport.getSportId()).getSportName());
            dto.setRemainSpots(venueSport.getRemainSpots());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<VenueSport> toEntity(List<VenueSportDTO> dtoSet){
        List<VenueSport> vs = new ArrayList<>();
        for(VenueSportDTO dto:dtoSet ){
            VenueSport entity = new VenueSport();
            entity.setVenueId(dto.getVenueId());
            entity.setSportId(sportMapper.findBySportName(dto.getSportName()).getId());
            entity.setRemainSpots(dto.getRemainSpots());
            vs.add(entity);
        }
        return vs;
    }

}
