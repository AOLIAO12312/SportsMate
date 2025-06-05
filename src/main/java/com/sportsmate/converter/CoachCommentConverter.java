package com.sportsmate.converter;

import com.sportsmate.dto.CoachCommentDTO;
import com.sportsmate.pojo.ReservationComment;
import com.sportsmate.pojo.User;
import com.sportsmate.service.ReservationCommentService;
import com.sportsmate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoachCommentConverter {
    @Autowired
    private UserService userService;


    public CoachCommentDTO toDTO(ReservationComment reservationComment){
        CoachCommentDTO dto = new CoachCommentDTO();
        Integer userId = reservationComment.getUserId();
        Integer coachId = reservationComment.getCoachId();

        User user = userService.findByUserId(userId);
        User coach = userService.findByUserId(coachId);

        dto.setUserId(userId);
        dto.setUsername(user.getUsername());
        dto.setCoachId(coachId);
        dto.setCoachName(coach.getUsername());
        dto.setCoachComment(reservationComment.getCoachComment());
        dto.setCoachRating(reservationComment.getCoachRating());
        return dto;
    }
}
