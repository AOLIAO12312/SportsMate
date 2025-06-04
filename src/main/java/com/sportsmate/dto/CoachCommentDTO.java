package com.sportsmate.dto;

import lombok.Data;

@Data
public class CoachCommentDTO {
    Integer userId;
    String username;
    Integer coachId;
    String coachName;
    Integer coachRating;
    String coachComment;
}
