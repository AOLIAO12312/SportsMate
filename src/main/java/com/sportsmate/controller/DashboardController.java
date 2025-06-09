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

    private static final Map<String, Integer> STATIC_DAU_MAP = new LinkedHashMap<>();
    private static final Map<String, Object> STATIC_MAU;

    static {
        // 初始化 2025-06-01 到 2025-07-01 的每日活跃数据（模拟固定值）
        LocalDate start = LocalDate.of(2025, 6, 1);
        LocalDate end = LocalDate.of(2025, 7, 1);
        Random random = new Random(42); // 固定种子，保证每次启动结果一样

        LocalDate current = start;
        while (!current.isAfter(end)) {
            STATIC_DAU_MAP.put(current.toString(), random.nextInt(300) + 100); // 100 ~ 399
            current = current.plusDays(1);
        }

        // 模拟6月月活（例如以所有DAU用户总数的估计）
        STATIC_MAU = new HashMap<>();
        STATIC_MAU.put("month", "JUNE");
        STATIC_MAU.put("monthlyActiveUsers", 2450); // 可改为 sum 或唯一用户估计值
    }

    /**
     * 返回当天（2025-06-09）活跃数据（固定）
     */
    @GetMapping("/dau")
    public Result getDailyActiveUsers() {
        String today = LocalDate.now().toString(); // 固定返回该日
        Map<String, Object> response = new HashMap<>();
        response.put("date", today);
        response.put("dailyActiveUsers", STATIC_DAU_MAP.get(today));
        return Result.success(response);
    }

    /**
     * 返回六月的月活数据（固定）
     */
    @GetMapping("/mau")
    public Result getMonthlyActiveUsers() {
        return Result.success(STATIC_MAU);
    }

    /**
     * 返回最近7天的趋势（从静态DAU中截取）
     */
    @GetMapping("/weekly-trend")
    public Result getWeeklyTrend() {
        List<Map<String, Object>> trend = new ArrayList<>();

        LocalDate end = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = end.minusDays(i);
            Map<String, Object> day = new HashMap<>();
            day.put("date", date.toString());
            day.put("activeUsers", STATIC_DAU_MAP.get(date.toString()));
            trend.add(day);
        }

        return Result.success(trend);
    }

    /**
     * （可选）返回整个月的DAU数据
     */
    @GetMapping("/dau/month")
    public Result getMonthlyDailyActiveUsers() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : STATIC_DAU_MAP.entrySet()) {
            Map<String, Object> day = new HashMap<>();
            day.put("date", entry.getKey());
            day.put("dailyActiveUsers", entry.getValue());
            list.add(day);
        }
        return Result.success(list);
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
