package com.sportsmate.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sportsmate.dto.VenueDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coach_reservation")
public class CoachReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 预约ID

    @NotNull
    private Integer userId;  // 发起预约的用户ID（外键，关联到用户表的id）
    @NotNull
    private Integer coachId;  // 预约的教练ID（外键，关联到用户表的id）

    private LocalDateTime startTime;  // 预约起始时间

    private LocalDateTime endTime;    // 预约结束时间

    private ReservationStatus status = ReservationStatus.待确认;  // 预约状态（枚举类型）

    @JsonIgnore
    private Integer venueId;

    private VenueDTO venueDTO;
}
