package com.java2nb.novel.service;

import com.java2nb.novel.mapper.AdminStatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
        int days = parseDays(daysStr);
        LocalDate start = LocalDate.now().minusDays(days - 1L);
        String startStr = start.format(DAY_FMT) + " 00:00:00";

        // 完整日期轴
        List<String> dates = buildDateAxis(start, days);

        Map<String, Object> result = new HashMap<>(8);
        result.put("dates", dates);
        for (Map.Entry<String, String> entry : TREND_TABLES.entrySet()) {
            List<Map<String, Object>> rows = statMapper.dailyCount(entry.getValue(), startStr);
            Map<String, Object> countMap = new HashMap<>(rows.size());
            for (Map<String, Object> row : rows) {
                countMap.put(String.valueOf(row.get("d")), intValue(row.get("cnt")));
            }
            List<Object> series = new ArrayList<>(days);
            for (String day : dates) {
                series.add(countMap.getOrDefault(day, 0));
            }
            result.put(entry.getKey(), series);
        }
        return result;
    }

    /**
     * 首页扩展统计：随天数变化的充值金额/笔数趋势，与不随天数变化的分类分布、热门作品TOP、支付渠道占比。
     * 数值统一转 Integer，规避项目全局 Jackson Long→String 序列化对图表取数的干扰
     *
     * @param daysStr 天数字符串（7~90，非法值回退30）
     * @return {dates/pay/categories/hotBooks/channels}
     */
    public Map<String, Object> extra(String daysStr) {
        int days = parseDays(daysStr);
        LocalDate start = LocalDate.now().minusDays(days - 1L);
        String startStr = start.format(DAY_FMT) + " 00:00:00";
        List<String> dates = buildDateAxis(start, days);

        // 充值趋势：无支付的日期补 0
        Map<String, Object> amountMap = new HashMap<>(dates.size());
        Map<String, Object> countMap = new HashMap<>(dates.size());
        for (Map<String, Object> row : statMapper.payDaily(startStr)) {
            amountMap.put(String.valueOf(row.get("d")), intValue(row.get("amount")));
            countMap.put(String.valueOf(row.get("d")), intValue(row.get("cnt")));
        }
        List<Object> amounts = new ArrayList<>(days);
        List<Object> counts = new ArrayList<>(days);
        for (String day : dates) {
            amounts.add(amountMap.getOrDefault(day, 0));
            counts.add(countMap.getOrDefault(day, 0));
        }
        Map<String, Object> pay = new HashMap<>(4);
        pay.put("amount", amounts);
        pay.put("count", counts);

        List<Map<String, Object>> categories = new ArrayList<>();
        for (Map<String, Object> row : statMapper.categoryDist()) {
            categories.add(row2(row.get("name"), intValue(row.get("value"))));
        }

        List<Map<String, Object>> hotBooks = new ArrayList<>();
        for (Map<String, Object> row : statMapper.hotBooks(10)) {
            Map<String, Object> item = new LinkedHashMap<>(4);
            item.put("name", row.get("bookName"));
            item.put("author", row.get("authorName"));
            item.put("visits", intValue(row.get("visits")));
            hotBooks.add(item);
        }

        List<Map<String, Object>> channels = new ArrayList<>();
        for (Map<String, Object> row : statMapper.channelDist()) {
            Map<String, Object> item = new LinkedHashMap<>(4);
            item.put("name", row.get("name"));
            item.put("value", intValue(row.get("value")));
            item.put("amount", intValue(row.get("amount")));
            channels.add(item);
        }

        Map<String, Object> result = new LinkedHashMap<>(8);
        result.put("dates", dates);
        result.put("pay", pay);
        result.put("categories", categories);
        result.put("hotBooks", hotBooks);
        result.put("channels", channels);
        return result;
    }

    /**
     * 天数解析：7~90，非法值回退30
     */
    private int parseDays(String daysStr) {
        int days;
        try {
            days = Integer.parseInt(daysStr.trim());
        } catch (NumberFormatException | NullPointerException e) {
            days = 30;
        }
        if (days < 7 || days > 90) {
            days = 30;
        }
        return days;
    }

    /**
     * 从 start 起连续 days 天的日期轴（yyyy-MM-dd）
     */
    private List<String> buildDateAxis(LocalDate start, int days) {
        List<String> dates = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            dates.add(start.plusDays(i).format(DAY_FMT));
        }
        return dates;
    }

    /**
     * 项 {name, value} 小工具
     */
    private Map<String, Object> row2(Object name, int value) {
        Map<String, Object> item = new LinkedHashMap<>(4);
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    /**
     * 项目全局 Jackson 会把 Long 序列化成字符串（防JS精度丢失），计数统一用 Integer 承载保证输出数值
     */
    private static int intValue(Object v) {
        return v instanceof Number ? ((Number) v).intValue() : Integer.parseInt(String.valueOf(v));
    }

    /**
     * 首页概览卡片：作品/会员总数与今日新增、累计与今日充值金额、累计与今日支付笔数。
     * 表名均为 Service 内部硬编码，不走外部参数
     *
     * @return 概览指标 Map
     */
    public Map<String, Object> overview() {
        String today = LocalDate.now().format(DAY_FMT) + " 00:00:00";
        Map<String, Object> result = new LinkedHashMap<>(10);
        result.put("bookCount", statMapper.countAll("book"));
        result.put("bookToday", statMapper.countToday("book", today));
        result.put("userCount", statMapper.countAll("user"));
        result.put("userToday", statMapper.countToday("user", today));
        Map<String, Object> pay = statMapper.payStat(today);
        result.put("payAmount", pay.get("amount"));
        result.put("payAmountToday", pay.get("todayAmount"));
        result.put("payCount", pay.get("payCount"));
        result.put("payCountToday", pay.get("todayCount"));
        return result;
    }

}
