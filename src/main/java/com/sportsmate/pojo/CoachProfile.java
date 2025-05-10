package com.sportsmate.pojo;

import lombok.Data;

import javax.persistence.*;
@Data
@Entity
@Table(name = "coach_profiles")
public class CoachProfile {

    @Id
    private Integer userId;

    @Column(name = "real_name", nullable = false, length = 20)
    private String realName;

    @Column(name = "profile_description", columnDefinition = "TEXT")
    private String profileDescription;

    @ManyToOne
    @JoinColumn(name = "coached_sports", nullable = false)
    private Sport coachedSports;

    @Column(nullable = false)
    private Float rating = 5.0f;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
