package com.sportsmate.dto;

import com.sportsmate.pojo.Gender;
import com.sportsmate.pojo.SuccessfulMatchStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class SuccessfulMatchDTO {

    @Id
    private Integer id;  // 匹配成功ID

    @NotNull
    private String opponentName;  // 对手

    private Gender opponentGender;

    private Integer opponentRankScore;

    private String opponentPhone;

    @NotNull
    private String sportName;  // 运动类型

    @NotNull
    private String venueName;  // 场馆

    @Column(nullable = false)
    private LocalDateTime startTime;  // 起始时间

    @Column(nullable = false)
    private LocalDateTime endTime;  // 结束时间

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuccessfulMatchStatus status = SuccessfulMatchStatus.待完成;  // 匹配状态（默认'待完成'）

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 创建时间
}
