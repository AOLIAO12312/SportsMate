package com.sportsmate.service;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.PageBean;
import com.sportsmate.pojo.SuccessfulMatch;

import java.util.List;

public interface MatchService {
    void addRequest(MatchRequestDTO dto);

    MatchRequest findActiveRequestByUserId(Integer loginUserId);

    PageBean<MatchRequestDTO> listRequests(Integer pageNum, Integer pageSize);

    MatchRequest findById(Integer requestId);

    void cancel(Integer requestId, String remark);

    void addRequestWithAutoMatch(MatchRequestDTO dto, Integer loginUserId);

    PageBean<SuccessfulMatchDTO> listSuccessfulMatches(Integer pageNum, Integer pageSize);

    List<SuccessfulMatch> findActiveMatchByUserId(Integer loginUserId);
}
