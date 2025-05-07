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

    // 主键由组合键（venue_id, sport_id）来定义，JPA 会自动管理这个复合主键
}
