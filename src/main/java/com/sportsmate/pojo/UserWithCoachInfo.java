
package com.sportsmate.pojo;

import lombok.Data;

@Data
public class UserWithCoachInfo {
    private User user;
    private CoachProfile coachProfile;
}