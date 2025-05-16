package com.sportsmate.pojo;

import java.time.DayOfWeek;

public enum Weekday {
    星期一, 星期二, 星期三, 星期四, 星期五, 星期六, 星期天;

    public DayOfWeek toJavaDayOfWeek() {
        switch (this) {
            case 星期一: return DayOfWeek.MONDAY;
            case 星期二: return DayOfWeek.TUESDAY;
            case 星期三: return DayOfWeek.WEDNESDAY;
            case 星期四: return DayOfWeek.THURSDAY;
            case 星期五: return DayOfWeek.FRIDAY;
            case 星期六: return DayOfWeek.SATURDAY;
            case 星期天: return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("无效的星期枚举");
        }
    }
}
