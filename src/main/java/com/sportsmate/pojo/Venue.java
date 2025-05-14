package com.sportsmate.pojo;

import com.sportsmate.dto.VenueSportDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "venue")
public class Venue {
    @Column(nullable = false)
    private Integer id;  // 场馆ID

    @NotNull
    private String name;    //场馆名称

    @NotNull
    private String country;  // 国家

    @NotNull
    private String state;  // 省/州

    @NotNull
    private String city;  // 市

    @NotNull
    private String district;  // 区/县

    @NotNull
    private String street;  // 街道

    @NotNull
    private String postalCode;  // 邮政编码

    @NotNull
    private String fullAddress;

    @Column(nullable = false)
    @Pattern(regexp = "^\\d{1,2}:\\d{2}$")
    private String openingTime;  // 每日开放时间（例如：08:00）

    @Column(nullable = false)
    @Pattern(regexp = "^\\d{1,2}:\\d{2}$")
    private String closingTime;  // 每日关闭时间（例如：22:00）

    private String notes;  // 备注（例如“周一闭馆”）

    @Pattern(regexp = "^\\+?\\d{1,4}[-\\s]?\\d{1,15}$")
    private String phone;  // 联系电话（可为空）

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间

    @OneToMany(mappedBy = "venue")
    private List<VenueSportDTO> venueSportDTOSet;  // 场馆对应的运动类型
}
