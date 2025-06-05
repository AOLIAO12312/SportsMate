package com.sportsmate.dto;

import lombok.Data;

@Data
public class CoachProfileDTO {
    private Integer userId;
    private String realName;
    private String phone;
    private String profileDescription;
    private String coachedSportName;
    // 添加教练地址字段
    private String address;
    private Float rating;
    private Boolean isActive;
}

