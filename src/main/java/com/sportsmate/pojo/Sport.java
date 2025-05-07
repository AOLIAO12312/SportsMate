package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Sport {
    @NotNull
    private Integer id;//主键ID
    private String name;//运动名称
    private String description;//运动描述
}
