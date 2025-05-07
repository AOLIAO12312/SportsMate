package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class Venue {
    @NotNull
    private Integer id;  // 场馆ID

    @NotNull
    private String address;  // 场馆地址

    @NotNull
    @Pattern(regexp = "^\\d{1,2}:\\d{2}$")
    private String openingTime;  // 每日开放时间（例如：08:00）

    @NotNull
    @Pattern(regexp = "^\\d{1,2}:\\d{2}$")
    private String closingTime;  // 每日关闭时间（例如：22:00）

    private String notes;  // 备注（例如“周一闭馆”）

    private Integer remainingSpots = 0;  // 剩余位置（默认0）

    @Pattern(regexp = "^\\+?\\d{1,4}[-\\s]?\\d{1,15}$")
    private String phone;  // 联系电话（可为空）

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间

    @OneToMany(mappedBy = "venue")
    private Set<VenueSport> venueSports;  // 场馆对应的运动类型
}
