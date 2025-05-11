package com.sportsmate.mapper;

import com.sportsmate.pojo.Sport;
import jakarta.servlet.http.PushBuilder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SportMapper {

    @Select("select * from sports where name=#{name}")
    public Sport findBySportName(String name);

    @Select("select * from sports where id=#{id}")
    public Sport findBySportId(Integer id);
}
