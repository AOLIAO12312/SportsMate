package com.sportsmate.pojo;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "available_time")
public class AvailableTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 记录ID

    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id")
    private User coach;  // 教练ID (外键，关联到用户表的id)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weekday weekday;  // 星期几（枚举类型）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot timeSlot;  // 时间段（枚举类型）

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间
}
