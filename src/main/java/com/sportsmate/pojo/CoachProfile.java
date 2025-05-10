package com.sportsmate.pojo;

import javax.persistence.*;

@Entity
@Table(name = "coach_profiles")
public class CoachProfile {

    @Id
    private Integer userId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "real_name", nullable = false, length = 20)
    private String realName;

    @Column(name = "profile_description", columnDefinition = "TEXT")
    private String profileDescription;

    @ManyToOne
    @JoinColumn(name = "coached_sports", nullable = false)
    private Sport coachedSport;

    @Column(nullable = false)
    private Float rating = 5.0f;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Getter 和 Setter 省略
}
