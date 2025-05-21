package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class ReservationComment {
    @NotNull
    private Integer id;  // 评价ID

    @NotNull
    private Integer userId;  // 用户ID（外键）

    @NotNull
    private Integer coachId;  // 教练ID（外键）

    @NotNull
    private Integer coachReservationId; // 指向coach_reservation_id

    @NotNull
    @Min(0)
    @Max(10)
    private Integer coachRating;  // 教练评分（0-10）

    private String coachComment;  // 对教练的文字评价

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}