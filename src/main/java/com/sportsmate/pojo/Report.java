package com.sportsmate.pojo;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class Report {

    @NotNull
    private Integer id;

    @NotNull
    private Integer reporterId;

    @NotNull
    private Integer reportedId;

    @NotNull
    private String reason;

    private Integer commentId;

    private Integer matchId;

    public boolean isValid() {
        return (matchId != null && commentId == null) || (matchId == null && commentId != null);
    }//判断必须有两个中的一个

    @NotNull
    @Enumerated(EnumType.STRING)
    private HandleStatus status = HandleStatus.未处理;

    private String replyMessage;
}