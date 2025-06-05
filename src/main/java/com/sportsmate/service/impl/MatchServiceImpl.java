package com.sportsmate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sportsmate.converter.SuccessfulMatchConverter;
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

import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public PageBean<MatchRequestDTO> listRequests(Integer pageNum, Integer pageSize, String status) {
        PageBean<MatchRequestDTO> pb = new PageBean<>();

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);

        // 获取当前登录用户 ID（从 ThreadLocal 中）
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        // 将字符串 status 转换为枚举
        MatchRequestStatus requestStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                requestStatus = MatchRequestStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("非法的状态参数：" + status);
            }
        }

        // 根据用户ID + 状态查询请求列表
        List<MatchRequest> as = matchRequestMapper.listByUserIdAndStatus(loginUserId, requestStatus);

        // 使用 PageInfo 获取分页信息
        PageInfo<MatchRequest> pageInfo = new PageInfo<>(as);

        // 转换成 DTO 列表
        List<MatchRequestDTO> dtos = new ArrayList<>();
        for (MatchRequest matchRequest : as) {
            MatchRequestDTO dto = matchRequestConverter.toDTO(matchRequest);
            dtos.add(dto);
        }

        // 封装返回结果
        pb.setTotal(pageInfo.getTotal());
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



        SuccessfulMatch successfulMatch =  successfulMatchMapper.findByRequestId(requestId);
        if(successfulMatch != null){
            successfulMatchMapper.cancel(successfulMatch.getId());
            if(Objects.equals(requestId,successfulMatch.getMatchRequestId1())){
                matchRequestMapper.cancel(successfulMatch.getMatchRequestId2(),"对方原因:"+remark);
            }else {
                matchRequestMapper.cancel(successfulMatch.getMatchRequestId1(),"对方原因:"+remark);
            }
            //减少本人的信誉积分
            Map<String,Integer> claims = ThreadLocalUtil.get();
            Integer loginId = claims.get("id");
            // 查询并修改信誉积分
            User user = userMapper.findByUserId(loginId);
            if (user != null) {
                int newReputationScore = user.getReputationScore() - 5; // 🚨 每次取消扣5分（你可以调整数值）

                // 最低不低于0分
                newReputationScore = Math.max(newReputationScore, 0);
                user.setReputationScore(newReputationScore);
                userMapper.updateReputationScore(user.getId(), newReputationScore);

                // 判断信誉等级
                if (newReputationScore < 80) {
                    // 封禁用户
                    userMapper.updateStatus(user.getId(), UserStatus.封禁);
                    // 你可以记录封禁日志或发送通知
                } else if (newReputationScore < 90) {
                    userMapper.updateStatus(user.getId(), UserStatus.警告);
                }
            }
        }
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
                incoming.getVenueId(),
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
                successfulMatch.setVenueId(candidate.getVenueId());
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
    public PageBean<SuccessfulMatchDTO> listSuccessfulMatches(Integer pageNum, Integer pageSize, String status) {
        PageBean<SuccessfulMatchDTO> pb = new PageBean<>();

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);

        // 获取当前登录用户ID
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer loginUserId = (Integer) claims.get("id");

        // 解析枚举状态
        SuccessfulMatchStatus matchStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                matchStatus = SuccessfulMatchStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("非法的状态参数：" + status);
            }
        }

        // 执行查询
        List<SuccessfulMatch> matches = successfulMatchMapper.listByUserIdAndStatus(loginUserId, matchStatus);

        // 使用 PageInfo 获取分页信息
        PageInfo<SuccessfulMatch> pageInfo = new PageInfo<>(matches);

        // 转 DTO 列表
        List<SuccessfulMatchDTO> dtos = new ArrayList<>();
        for (SuccessfulMatch match : matches) {
            SuccessfulMatchDTO dto = successfulMatchConverter.toDTO(match);
            dtos.add(dto);
        }

        pb.setTotal(pageInfo.getTotal());
        pb.setItems(dtos);
        return pb;
    }


    @Override
    public List<SuccessfulMatch> findActiveMatchByUserId(Integer loginUserId) {
        return successfulMatchMapper.findActiveMatchByUserId(loginUserId);
    }

    private boolean isMutuallyMatched(MatchRequest a, MatchRequest b) {
        return a.getExpectedOpponentGender() == userMapper.findByUserId(b.getUserId()).getGender()  &&
                b.getExpectedOpponentGender() == userMapper.findByUserId(a.getUserId()).getGender() &&
                a.getSportId().equals(b.getSportId()) && Objects.equals(a.getVenueId(), b.getVenueId()) &&
                timeOverlaps(a.getStartTime(), a.getEndTime(), b.getStartTime(), b.getEndTime());
    }

    private boolean timeOverlaps(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }


}
