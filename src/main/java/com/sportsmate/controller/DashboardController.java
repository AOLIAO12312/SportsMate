package com.sportsmate.controller;

import com.sportsmate.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    /**
     * 模拟返回当天日活跃用户数量
     */
    @GetMapping("/dau")
    public Result getDailyActiveUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("dailyActiveUsers", new Random().nextInt(300) + 100); // 模拟100~399之间的DAU
        return Result.success(response);
    }

    /**
     * 模拟返回本月月活跃用户数量
     */
    @GetMapping("/mau")
    public Result getMonthlyActiveUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("month", LocalDate.now().getMonth().toString());
        response.put("monthlyActiveUsers", new Random().nextInt(2000) + 1000); // 模拟1000~2999之间的MAU
        return Result.success(response);
    }

    /**
     * 模拟返回最近7天的活跃用户趋势数据（折线图用）
     */
    @GetMapping("/weekly-trend")
    public Result getWeeklyTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        Random random = new Random();

        for (int i = 6; i >= 0; i--) {
            Map<String, Object> dayData = new HashMap<>();
            LocalDate date = today.minusDays(i);
            dayData.put("date", date.toString());
            dayData.put("activeUsers", random.nextInt(300) + 100); // 100~399
            trend.add(dayData);
        }

        return Result.success(trend);
    }

    /**
     * 模拟返回地域热力图数据
     */
    @GetMapping("/location-heatmap")
    public Result getLocationHeatmapData() {
        List<String> provinces = Arrays.asList(
                "北京", "上海", "广东", "浙江", "江苏", "四川", "山东", "湖北", "陕西", "福建"
        );
        List<Map<String, Object>> heatmap = new ArrayList<>();
        Random random = new Random();
        for (String province : provinces) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("region", province);
            entry.put("activeUsers", random.nextInt(500) + 100); // 模拟100~599活跃用户
            heatmap.add(entry);
        }
        return Result.success(heatmap);
    }

    /**
     * 模拟返回某用户的本周运动概况（如总运动时长、运动天数、平均每日时长等）
     */
    @GetMapping("/user-weekly-summary")
    public Result getUserWeeklySummary() {
        Random random = new Random();
        int totalMinutes = random.nextInt(300) + 120; // 总运动时间 120 ~ 420 分钟
        int activeDays = random.nextInt(5) + 2; // 活跃天数 2 ~ 6 天

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalMinutes", totalMinutes);
        summary.put("activeDays", activeDays);
        summary.put("averageMinutesPerDay", totalMinutes / activeDays);
        summary.put("caloriesBurned", totalMinutes * 5); // 粗略估计热量消耗（1分钟约消耗5卡）
        summary.put("week", getCurrentWeekRange());

        return Result.success(summary);
    }

    /**
     * 模拟返回某用户的运动类型及时间占比数据（如羽毛球/乒乓球/跑步）
     */
    @GetMapping("/user-activity-stats")
    public Result getUserActivityStats() {
        List<String> sports = Arrays.asList("羽毛球", "乒乓球", "跑步", "健身", "篮球");
        Random random = new Random();

        List<Map<String, Object>> stats = new ArrayList<>();
        int total = 0;

        // 先用 HashMap 保存每项数据
        for (String sport : sports) {
            int minutes = random.nextInt(120); // 每种运动 0 ~ 119 分钟
            Map<String, Object> entry = new HashMap<>();
            entry.put("sport", sport);
            entry.put("minutes", minutes);
            stats.add(entry);
            total += minutes;
        }

        // 再计算百分比
        for (Map<String, Object> entry : stats) {
            int minutes = (int) entry.get("minutes");
            double percent = total == 0 ? 0.0 : (minutes * 100.0 / total);
            entry.put("percent", Math.round(percent * 10.0) / 10.0); // 保留一位小数
        }

        return Result.success(stats);
    }


    /**
     * 工具方法：获取本周日期范围
     */
    private String getCurrentWeekRange() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);
        return monday + " ~ " + sunday;
    }
}
