package com.sportsmate.utils;

import com.sportsmate.pojo.Weekday;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {
    public static LocalDateTime getNextDateTime(Weekday targetWeekday, LocalTime time) {
        LocalDate today = LocalDate.now();
        DayOfWeek todayDayOfWeek = today.getDayOfWeek(); // MONDAY...SUNDAY

        // 将 Weekday 枚举转为 DayOfWeek（1=Monday, ..., 7=Sunday）
        int targetDay = targetWeekday.toJavaDayOfWeek().getValue();
        int todayDay = todayDayOfWeek.getValue();

        // 计算距离目标星期几还有几天
        int daysUntil = (targetDay - todayDay + 7) % 7;
        if (daysUntil == 0 && time.isBefore(LocalTime.now())) {
            // 今天的时间已过，则约到下周
            daysUntil = 7;
        }

        LocalDate targetDate = today.plusDays(daysUntil);
        return LocalDateTime.of(targetDate, time);
    }

}
