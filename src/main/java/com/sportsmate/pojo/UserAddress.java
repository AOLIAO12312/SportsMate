package com.sportsmate.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_addresses")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;  // 地址ID

    @JsonIgnore
    private Integer userId;  // 用户ID（外键，关联到用户表的id）

    @Column(nullable = false)
    private String country;  // 国家

    private String state;  // 省/州

    private String city;  // 市

    private String district;  // 区/县

    private String street;  // 街道

    private String postalCode;  // 邮政编码

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType = AddressType.默认;  // 地址类型（枚举类型）

    private LocalDateTime createdAt;  // 创建时间

    private LocalDateTime updatedAt;  // 更新时间
}
