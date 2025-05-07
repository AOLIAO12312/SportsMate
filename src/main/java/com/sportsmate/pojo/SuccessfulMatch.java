package com.sportsmate.pojo;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "successful_matches")
public class SuccessfulMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // 匹配成功ID

    @ManyToOne
    @JoinColumn(name = "match_request_id1", referencedColumnName = "id", nullable = false)
    private MatchRequest matchRequest1;  // 匹配发起ID1（外键）

    @ManyToOne
    @JoinColumn(name = "match_request_id2", referencedColumnName = "id", nullable = false)
    private MatchRequest matchRequest2;  // 匹配发起ID2（外键）

    @ManyToOne
    @JoinColumn(name = "user_id1", referencedColumnName = "id", nullable = false)
    private User user1;  // 匹配用户ID1（外键）

    @ManyToOne
    @JoinColumn(name = "user_id2", referencedColumnName = "id", nullable = false)
    private User user2;  // 匹配用户ID2（外键）

    @ManyToOne
    @JoinColumn(name = "sport_id", referencedColumnName = "id", nullable = false)
    private Sport sport;  // 运动类型（外键）

    @ManyToOne
    @JoinColumn(name = "venue_id", referencedColumnName = "id", nullable = false)
    private Venue venue;  // 场馆ID（外键）

    @Column(nullable = false)
    private LocalDateTime startTime;  // 起始时间

    @Column(nullable = false)
    private LocalDateTime endTime;  // 结束时间

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuccessfulMatchStatus status = SuccessfulMatchStatus.待完成;  // 匹配状态（默认'待完成'）

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 创建时间
}
