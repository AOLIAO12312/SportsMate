package com.sportsmate.mapper;

import com.sportsmate.dto.VenueDTO;
import com.sportsmate.pojo.Venue;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface VenueMapper {

    @Select("select * from venues where name=#{name}")
    Venue findByName(String name);

    @Insert("INSERT INTO venues (name, country, state, city, district, street, postal_code, full_address, opening_time, closing_time, notes, phone, created_at, updated_at) " +
            "VALUES (#{name}, #{country}, #{state}, #{city}, #{district}, #{street}, #{postalCode}, #{fullAddress}, #{openingTime}, #{closingTime}, #{notes}, #{phone}, NOW(), NOW())")
    void add(Venue venue);

    @Update("update venues set name=#{name},country=#{country},state=#{state},city=#{city},district=#{district},street=#{street},postal_code=#{postalCode},full_address=#{fullAddress},opening_time=#{openingTime},closing_time=#{closingTime},notes=#{notes},phone=#{phone},updated_at=#{updatedAt}")
    void update(Venue venue);

    @Select("SELECT name, opening_time,closing_time,full_address FROM venues")
    List<VenueDTO> listSimple();
}
