package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class MatchComment {
    @NotNull
    private Integer id;  // 评价ID

    @NotNull
    private Integer userId;  // 用户ID（外键）

    @NotNull
    private Integer matchId;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer opponentOrCoachRating;  // 对手/教练评分（0-10）

    @NotNull
    @Min(0)
    @Max(10)
    private Integer venueRating;  // 场馆评分（0-10）

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
