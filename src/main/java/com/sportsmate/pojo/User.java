package com.sportsmate.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @NotNull
    private Integer id;//主键ID
    private String username;//用户名
    private Gender gender = Gender.其他;//性别（“男”、“女”、“其他”）
    private UserType userType = UserType.普通用户;//用户类别（“普通用户”，“教练”，“管理员”）
    private UserStatus userStatus = UserStatus.正常;//用户状态，正常，警告，封禁


    @Pattern(regexp = "^\\S{1,20}$")
    private String realName;//真实姓名（教练必须填写）
    private Integer coachedSports;
    private String profileDescription;//教练个人介绍

    @Pattern(regexp = "^\\+?\\d{1,4}[-\\s]?\\d{1,15}$")
    private String phone;


    @JsonIgnore
    private String password;//密码

    @NotNull
    private Integer rankScore;//段位分
    private Integer reputationScore;//信誉分

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "coach")
    private Set<AvailableTime> availableTimes;  // 教练的所有空闲时间

    @OneToMany(mappedBy = "user")
    private Set<UserAddress> addresses;  // 用户的所有地址

    @OneToMany(mappedBy = "user")
    private Set<CoachReservation> coachReservations;  // 发起的预约

    @OneToMany(mappedBy = "coach")
    private Set<CoachReservation> reservationsAsCoach;  // 作为教练的预约

    @OneToMany(mappedBy = "user")
    private Set<MatchRequest> matchRequests;  // 发起的匹配请求

    // 关联成功匹配的用户
    @OneToMany(mappedBy = "user1")
    private List<SuccessfulMatch> successfulMatchesAsUser1;  // 作为用户1参与的所有成功匹配

    @OneToMany(mappedBy = "user2")
    private List<SuccessfulMatch> successfulMatchesAsUser2;  // 作为用户2参与的所有成功匹配
}
