package com.sportsmate.pojo;

import javax.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "successful_matches")
public class SuccessfulMatch {

    @Id
    private Integer id;  // 匹配成功ID

    @NotNull
    private Integer matchRequestId1;  // 匹配发起ID1（外键）

    @NotNull
    private Integer matchRequestId2;  // 匹配发起ID2（外键）

    @NotNull
    private Integer userId1;  // 匹配用户ID1（外键）

    @NotNull
    private Integer userId2;  // 匹配用户ID2（外键）

    @NotNull
    private Integer sportId;  // 运动类型（外键）

    @NotNull
    private Integer venueId;  // 场馆ID（外键）

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
