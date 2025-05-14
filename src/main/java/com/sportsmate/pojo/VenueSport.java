package com.sportsmate.pojo;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "venue_sports")
public class VenueSport {

    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id", nullable = false)
    private Venue venue;  // 场馆（外键，关联到venues表）

    @ManyToOne
    @JoinColumn(name = "sport_id", referencedColumnName = "id", nullable = false)
    private Sport sport;  // 运动类型（外键，关联到sports表）

    private Integer remainSpots = 0;  // 剩余位置（默认0）

}
