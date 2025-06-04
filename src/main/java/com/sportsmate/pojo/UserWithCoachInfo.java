
package com.sportsmate.pojo;

import com.sportsmate.dto.CoachProfileDTO;
import lombok.Data;

@Data
public class UserWithCoachInfo {
    private User user;
    private CoachProfileDTO coachProfileDTO;
}