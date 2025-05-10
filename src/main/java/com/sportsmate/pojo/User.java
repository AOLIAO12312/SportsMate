package com.sportsmate.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender = Gender.其他;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.正常;

    @Column(unique = true, length = 15)
    @Pattern(regexp = "\\d{6,12}", message = "手机号必须为6到12位数字")
    private String phone;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "rank_score")
    private Integer rankScore = 1200;

    @Column(name = "reputation_score")
    private Integer reputationScore = 100;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // 以下非必要字段统一忽略 JSON 返回，避免过多冗余数据暴露

    @JsonIgnore
    @OneToMany(mappedBy = "coach")
    private Set<AvailableTime> availableTimes;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<UserAddress> addresses;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<CoachReservation> coachReservations;

    @JsonIgnore
    @OneToMany(mappedBy = "coach")
    private Set<CoachReservation> reservationsAsCoach;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<MatchRequest> matchRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "user1")
    private List<SuccessfulMatch> successfulMatchesAsUser1;

    @JsonIgnore
    @OneToMany(mappedBy = "user2")
    private List<SuccessfulMatch> successfulMatchesAsUser2;
}
