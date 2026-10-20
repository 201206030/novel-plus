package com.java2nb.novel.service;

import com.java2nb.novel.mapper.AdminStatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台首页统计Service
 *
 * @author Administrator
 */
@Service
@RequiredArgsConstructor
public class StatService {

    private final AdminStatMapper statMapper;

    /**
     * 统计项 -> 表名白名单，杜绝任意表名注入
     */
    private static final Map<String, String> TREND_TABLES = Map.of(
            "book", "book",
            "user", "user",
            "order", "order_pay"
    );

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 近 days 天作品/会员/订单每日新增趋势，缺失日期补 0
     *
     * @param daysStr 天数字符串（7~90，非法值回退30）
     * @return {dates: [...], book: [...], user: [...], order: [...]}
     */
    public Map<String, Object> trend(String daysStr) {
        int days;
        try {
            days = Integer.parseInt(daysStr.trim());
        } catch (NumberFormatException | NullPointerException e) {
            days = 30;
        }
        if (days < 7 || days > 90) {
            days = 30;
        }
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(days - 1L);
        String startStr = start.format(DAY_FMT) + " 00:00:00";

        // 完整日期轴
        List<String> dates = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            dates.add(start.plusDays(i).format(DAY_FMT));
        }

        Map<String, Object> result = new HashMap<>(8);
        result.put("dates", dates);
        for (Map.Entry<String, String> entry : TREND_TABLES.entrySet()) {
            List<Map<String, Object>> rows = statMapper.dailyCount(entry.getValue(), startStr);
            Map<String, Object> countMap = new HashMap<>(rows.size());
            for (Map<String, Object> row : rows) {
                // 项目全局 Jackson 会把 Long 序列化成字符串（防JS精度丢失），计数用 Integer 承载保证输出数值
                Object cnt = row.get("cnt");
                int val = cnt instanceof Number ? ((Number) cnt).intValue() : Integer.parseInt(String.valueOf(cnt));
                countMap.put(String.valueOf(row.get("d")), val);
            }
            List<Object> series = new ArrayList<>(days);
            for (String day : dates) {
                series.add(countMap.getOrDefault(day, 0));
            }
            result.put(entry.getKey(), series);
        }
        return result;
    }

}
