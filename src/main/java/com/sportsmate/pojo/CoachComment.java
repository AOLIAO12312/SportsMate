package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoachComment {
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

    private LocalDateTime createdAt;  // 创建时间
}