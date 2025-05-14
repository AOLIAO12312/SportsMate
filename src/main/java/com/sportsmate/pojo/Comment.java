package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
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

    private String coachComment;  // 对教练的文字评价

    private LocalDateTime createdAt;  // 创建时间
}
