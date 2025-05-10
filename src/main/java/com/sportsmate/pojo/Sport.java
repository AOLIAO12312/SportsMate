package com.sportsmate.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Getter 和 Setter 省略
}
