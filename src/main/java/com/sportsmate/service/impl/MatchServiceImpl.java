package com.sportsmate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsmate.controller.SuccessfulMatchConverter;
import com.sportsmate.converter.MatchRequestConverter;
import com.sportsmate.dto.MatchRequestDTO;
import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.mapper.MatchRequestMapper;
import com.sportsmate.mapper.SuccessfulMatchMapper;
import com.sportsmate.mapper.UserMapper;
import com.sportsmate.pojo.*;
import com.sportsmate.service.MatchService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRequestConverter matchRequestConverter;

    @Autowired
    private MatchRequestMapper matchRequestMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SuccessfulMatchMapper successfulMatchMapper;

    @Autowired
    private SuccessfulMatchConverter successfulMatchConverter;

    @Override
    public void addRequest(MatchRequestDTO dto) {
        MatchRequest matchRequest = matchRequestConverter.toEntity(dto);

        Map<String,Object> claims =  ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        matchRequest.setUserId(loginUserId);
        matchRequest.setCreatedAt(LocalDateTime.now());
        matchRequestMapper.addRequest(matchRequest);
    }

    @Override
    public MatchRequest findActiveRequestByUserId(Integer loginUserId) {
        return matchRequestMapper.findActiveRequestByUserId(loginUserId);
    }

    @Override
    public PageBean<MatchRequestDTO> listRequests(Integer pageNum, Integer pageSize) {
        PageBean<MatchRequestDTO> pb = new PageBean<>();

        PageHelper.startPage(pageNum, pageSize);
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        // 这一步会被 PageHelper 自动分页
        List<MatchRequest> as = matchRequestMapper.list(loginUserId);

        // PageHelper 会返回 Page 类型（as 被代理）
        Page<MatchRequest> page = (Page<MatchRequest>) as;

        List<MatchRequestDTO> dtos = new ArrayList<>();
        for (MatchRequest matchRequest : as) {
            MatchRequestDTO dto = matchRequestConverter.toDTO(matchRequest);
            dtos.add(dto);
        }

        pb.setTotal(page.getTotal());
        pb.setItems(dtos);
        return pb;
    }

    @Override
    public MatchRequest findById(Integer requestId) {
        return matchRequestMapper.findById(requestId);
    }

    @Override
    public void cancel(Integer requestId, String remark) {
        matchRequestMapper.cancel(requestId,remark);
    }

    @Override
    public void addRequestWithAutoMatch(MatchRequestDTO dto, Integer loginUserId) {
        MatchRequest incoming = matchRequestConverter.toEntity(dto);
        incoming.setUserId(loginUserId);
        incoming.setCreatedAt(LocalDateTime.now());
        incoming.setStatus(MatchRequestStatus.待匹配);

        // 查询是否有匹配项
        List<MatchRequest> candidates = matchRequestMapper.findPotentialMatches(
                incoming.getSportId(),
                incoming.getExpectedOpponentGender(),
                incoming.getStartTime(),
                incoming.getEndTime(),
                loginUserId
        );

        for (MatchRequest candidate : candidates) {
            // 判断是否双方都互相符合
            if (isMutuallyMatched(candidate, incoming)) {
                // 设置双方状态为已完成
                candidate.setStatus(MatchRequestStatus.匹配成功);
                incoming.setStatus(MatchRequestStatus.匹配成功);

                matchRequestMapper.updateStatus(candidate);
                matchRequestMapper.addRequest(incoming);

                Integer user1 = incoming.getUserId();
                Integer user2 = candidate.getUserId();

                Integer requestId1 = incoming.getId();
                Integer requestId2 = candidate.getId();

                SuccessfulMatch successfulMatch = new SuccessfulMatch();

                successfulMatch.setUserId1(user1);
                successfulMatch.setUserId2(user2);
                successfulMatch.setMatchRequestId1(requestId1);
                successfulMatch.setMatchRequestId2(requestId2);
                successfulMatch.setSportId(incoming.getSportId());
                successfulMatch.setCreatedAt(LocalDateTime.now());
                successfulMatch.setStatus(SuccessfulMatchStatus.待完成);

                LocalDateTime startTime = incoming.getStartTime().isAfter(candidate.getStartTime()) ? incoming.getStartTime() : candidate.getStartTime();
                LocalDateTime endTime = incoming.getEndTime().isBefore(candidate.getEndTime()) ? incoming.getEndTime() : candidate.getEndTime();

                if (startTime.isBefore(endTime)) {
                    successfulMatch.setStartTime(startTime);
                    successfulMatch.setEndTime(endTime);
                } else {
                    throw new IllegalArgumentException("两个用户的时间段没有重合，无法匹配");
                }

                // 创建 successful_match 数据
                successfulMatchMapper.addSuccessfulMatch(successfulMatch);
                return;
            }
        }

        // 没有匹配项，正常插入待匹配请求
        matchRequestMapper.addRequest(incoming);
    }

    @Override
    public PageBean<SuccessfulMatchDTO> listSuccessfulMatches(Integer pageNum, Integer pageSize) {
        PageBean<SuccessfulMatchDTO> pb = new PageBean<>();

        PageHelper.startPage(pageNum, pageSize);
        Map<String,Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        // 这一步会被 PageHelper 自动分页
        List<SuccessfulMatch> as = successfulMatchMapper.list(loginUserId);

        // PageHelper 会返回 Page 类型（as 被代理）
        Page<SuccessfulMatch> page = (Page<SuccessfulMatch>) as;

        List<SuccessfulMatchDTO> dtos = new ArrayList<>();
        for (SuccessfulMatch successfulMatch : as) {
            SuccessfulMatchDTO dto = successfulMatchConverter.toDTO(successfulMatch);
            dtos.add(dto);
        }

        pb.setTotal(page.getTotal());
        pb.setItems(dtos);
        return pb;
    }

    private boolean isMutuallyMatched(MatchRequest a, MatchRequest b) {
        return a.getExpectedOpponentGender() == userMapper.findByUserId(b.getUserId()).getGender()  &&
                b.getExpectedOpponentGender() == userMapper.findByUserId(a.getUserId()).getGender() &&
                a.getSportId().equals(b.getSportId()) &&
                timeOverlaps(a.getStartTime(), a.getEndTime(), b.getStartTime(), b.getEndTime());
    }

    private boolean timeOverlaps(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }


}
