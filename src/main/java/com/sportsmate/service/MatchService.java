package com.sportsmate.service;

import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.pojo.MatchRequest;
import com.sportsmate.pojo.PageBean;

public interface MatchService {
    void addRequest(MatchRequestDTO dto);

    MatchRequest findActiveRequestByUserId(Integer loginUserId);

    PageBean<MatchRequestDTO> list(Integer pageNum, Integer pageSize);

    MatchRequest findById(Integer requestId);

    void cancel(Integer requestId, String remark);

    void addRequestWithAutoMatch(MatchRequestDTO dto, Integer loginUserId);
}
