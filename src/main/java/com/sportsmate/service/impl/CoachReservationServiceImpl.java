package com.sportsmate.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sportsmate.converter.CoachCommentConverter;
import com.sportsmate.dto.CoachCommentDTO;
import com.sportsmate.dto.SuccessfulMatchDTO;
import com.sportsmate.mapper.CoachReservationMapper;
import com.sportsmate.mapper.ReservationCommentMapper;
import com.sportsmate.pojo.*;
import com.sportsmate.service.CoachReservationService;
import com.sportsmate.service.ReservationCommentService;
import com.sportsmate.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sportsmate.utils.DateUtil.getNextDateTime;

@Service
public class CoachReservationServiceImpl implements CoachReservationService {

    @Autowired
    private CoachReservationMapper coachReservationMapper;

    @Autowired
    private ReservationCommentMapper reservationCommentMapper;

    @Autowired
    private CoachCommentConverter coachCommentConverter;

    @Override
    public void requestReservation(Integer loginUserId, AvailableTime availableTime) {
        LocalDateTime targetStartDateTime = getNextDateTime(availableTime.getWeekday(), availableTime.getStartTime());
        LocalDateTime targetEndDateTime = getNextDateTime(availableTime.getWeekday(),availableTime.getEndTime());
// 验证距离当前时间是否大于等于1小时
        Duration duration = Duration.between(LocalDateTime.now(), targetStartDateTime);

        if (duration.toMinutes() < 60) {
            throw new RuntimeException("预约时间必须距离当前时间至少1小时");
        }
        if(targetStartDateTime.isAfter(targetEndDateTime)){
            throw new RuntimeException("当前时间段不可预约");
        }
        Integer coachUserId = availableTime.getCoachId();

        CoachReservation existingCoachReservation =  coachReservationMapper.findByCoachIdAndDateTime(coachUserId,targetStartDateTime);
        if(existingCoachReservation != null){
            //抛出错误
            throw new RuntimeException("该时间段已被预约，请选择其他时间");
        }
        CoachReservation coachReservation = new CoachReservation();
        coachReservation.setCoachId(coachUserId);
        coachReservation.setUserId(loginUserId);
        coachReservation.setStartTime(targetStartDateTime);
        coachReservation.setEndTime(targetEndDateTime);
        coachReservationMapper.addReservation(coachReservation);
    }

    @Override
    public List<CoachReservation> findByUserId(Integer loginUserId) {
        return coachReservationMapper.findByUserId(loginUserId);
    }

    @Override
    public Object findByCoachId(Integer loginUserId) {
        return coachReservationMapper.findByCoachId(loginUserId);
    }

    @Override
    public void setStatus(CoachReservation coachReservation) {
        coachReservationMapper.setStatus(coachReservation);
    }

    @Override
    public CoachReservation findById(Integer reservationId) {
        return coachReservationMapper.findById(reservationId);
    }

    @Override
    public PageBean<CoachCommentDTO> getCoachCommentById(Integer pageNum, Integer pageSize, Integer coachId) {
        PageBean<CoachCommentDTO> pb = new PageBean<>();

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);

        if(coachId == null){
            return null;
        }

        // 执行查询
        List<ReservationComment> comments = reservationCommentMapper.findCommentByCoachId(coachId);

        // 使用 PageInfo 获取分页信息
        PageInfo<ReservationComment> pageInfo = new PageInfo<>(comments);

        // 转 DTO 列表
        List<CoachCommentDTO> dtos = new ArrayList<>();
        for (ReservationComment comment : comments) {
            CoachCommentDTO dto = coachCommentConverter.toDTO(comment);
            dtos.add(dto);
        }

        pb.setTotal(pageInfo.getTotal());
        pb.setItems(dtos);
        return pb;

    }

}
