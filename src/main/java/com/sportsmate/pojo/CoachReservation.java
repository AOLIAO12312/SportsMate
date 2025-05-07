package com.sportsmate.pojo;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coach_reservation")
public class CoachReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 预约ID

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;  // 发起预约的用户ID（外键，关联到用户表的id）

    @ManyToOne
    @JoinColumn(name = "coach_id", referencedColumnName = "id", nullable = false)
    private User coach;  // 预约的教练ID（外键，关联到用户表的id）

    private LocalDateTime startTime;  // 预约起始时间

    private LocalDateTime endTime;    // 预约结束时间

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status = ReservationStatus.待确认;  // 预约状态（枚举类型）

}
