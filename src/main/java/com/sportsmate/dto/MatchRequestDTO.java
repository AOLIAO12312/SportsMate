package com.sportsmate.dto;

import com.sportsmate.pojo.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class MatchRequestDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 匹配发起ID

    private Integer userId;  // 发起请求的用户ID（外键，关联到用户表）

    @NotNull
    private String sportName;  // 运动类型ID（外键，关联到运动表）

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender expectedOpponentGender = Gender.其他;  // 期望对手性别（枚举）

    private AddressType addressType = AddressType.默认;

    private LocalDateTime startTime;  // 匹配的起始时间

    private LocalDateTime endTime;    // 匹配的结束时间

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchRequestStatus status = MatchRequestStatus.待匹配;  // 匹配状态（枚举）

    private LocalDateTime createdAt;  // 创建时间

    private String remark;
}
