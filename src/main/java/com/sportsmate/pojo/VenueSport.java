package com.sportsmate.pojo;

import javax.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

@Data
@Entity
@Table(name = "venue_sports")
public class VenueSport {

    @NotNull
    private Integer venueId;

    @NotNull
    private Integer sportId;  // 运动类型

    private Integer remainSpots = 0;  // 剩余位置（默认0）

}
