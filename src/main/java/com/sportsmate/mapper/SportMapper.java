package com.sportsmate.mapper;

import com.sportsmate.pojo.Sport;
import jakarta.servlet.http.PushBuilder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SportMapper {

    @Select("select * from sports where sport_name=#{name}")
    public Sport findBySportName(String name);

    @Select("select * from sports where id=#{id}")
    public Sport findBySportId(Integer id);

    @Insert("insert into sports(sport_name,description) values(#{sportName},#{description})")
    void add(String sportName, String description);

    @Select("select sport_name from sports")
    List<String> findAllSportNames();

    @Select("select description from sports where sport_name=#{sportName}")
    String getDescription(String sportName);

    @Update("update sports set description=#{description} where sport_name=#{sportName}")
    void update(Sport sport);
}
