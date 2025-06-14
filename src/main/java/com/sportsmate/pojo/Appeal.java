package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import javax.persistence.Column;

import java.time.LocalDateTime;

@Data
public class Appeal {

    @NotNull
    private Integer id;

    @NotNull
    private Integer appellantId; // 申诉人id

    @NotNull
    private String reason; // 申诉理由

    @NotNull
    private HandleStatus status = HandleStatus.未处理; // 申诉状态

    private String replyMessage; // 新增：处理回复信息

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}