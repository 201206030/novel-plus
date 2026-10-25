package com.java2nb.novel.service;

import com.java2nb.novel.mapper.FrontAuthorDashboardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者创作数据看板Service：近7/30日更新字数、追读（阅读记录去重）、收藏趋势与断更/存稿提醒
 *
 * @author 11797
 */
@Service
@RequiredArgsConstructor
public class AuthorDashboardService {

    private final FrontAuthorDashboardMapper dashboardMapper;

    private static final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 近 days 天创作趋势：更新字数/追读人数/新增收藏，缺失日期补 0
     *
     * @param authorId 作家ID
     * @param days     天数（仅支持7或30，非法值回退30）
     * @return {dates, words, followReads, collects}
     */
    public Map<String, Object> trend(Long authorId, int days) {
        if (days != 7) {
            days = 30;
        }
        LocalDate start = LocalDate.now().minusDays(days - 1L);
        List<String> dates = buildDateAxis(start, days);
        String startStr = start.format(DAY_FMT) + " 00:00:00";

        Map<String, Object> result = new HashMap<>(8);
        result.put("dates", dates);
        result.put("words", fillSeries(dashboardMapper.dailyUpdateWords(authorId, startStr), dates, days));
        result.put("followReads", fillSeries(dashboardMapper.dailyFollowRead(authorId, startStr), dates, days));
        result.put("collects", fillSeries(dashboardMapper.dailyCollect(authorId, startStr), dates, days));
        return result;
    }

    /**
     * 看板摘要：连续更新天数/最近更新日期/今日与近30日更新字数/30日追读与收藏/存稿统计
     *
     * @param authorId 作家ID
     * @return 摘要数据（数值统一 Integer，规避全局 Long→String 序列化对图表取数的干扰）
     */
    public Map<String, Object> summary(Long authorId) {
        LocalDate today = LocalDate.now();
        String start30 = today.minusDays(29L).format(DAY_FMT) + " 00:00:00";
        String start60 = today.minusDays(59L).format(DAY_FMT) +  " 00:00:00";

        Map<String, Object> result = new HashMap<>(16);

        // 更新字数：今日/本周/近30日
        List<Map<String, Object>> wordRows = dashboardMapper.dailyUpdateWords(authorId, start30);
        Map<String, Integer> wordMap = toCountMap(wordRows);
        String todayStr = today.format(DAY_FMT);
        String weekStartStr = today.minusDays(6L).format(DAY_FMT);
        int todayWords = wordMap.getOrDefault(todayStr, 0);
        int weekWords = 0;
        int monthWords = 0;
        int followRead30 = 0;
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            monthWords += entry.getValue();
            if (entry.getKey().compareTo(weekStartStr) >= 0) {
                weekWords += entry.getValue();
            }
        }
        for (Integer v : toCountMap(dashboardMapper.dailyFollowRead(authorId, start30)).values()) {
            followRead30 += v;
        }
        int collect30 = sumValues(toCountMap(dashboardMapper.dailyCollect(authorId, start30)));

        // 连续更新天数：从今天（或昨天，容忍今天还没写）往前数连续有发布的自然日
        List<String> updateDates = dashboardMapper.updateDates(authorId, start60);
        Set<String> dateSet = new HashSet<>(updateDates);
        int streakDays = 0;
        LocalDate cursor = today;
        if (!dateSet.contains(todayStr)) {
            // 今天尚未更新时从昨天起算，不断更提醒由前端根据 gapDays 展示
            cursor = today.minusDays(1);
        }
        while (dateSet.contains(cursor.format(DAY_FMT))) {
            streakDays++;
            cursor = cursor.minusDays(1);
        }
        String lastUpdateDate = updateDates.isEmpty() ? null : updateDates.get(0);
        long gapDays = lastUpdateDate == null ? -1
            : java.time.temporal.ChronoUnit.DAYS.between(LocalDate.parse(lastUpdateDate, DAY_FMT), today);

        // 存稿统计
        Map<String, Object> draft = dashboardMapper.draftStat(authorId);

        result.put("streakDays", streakDays);
        result.put("lastUpdateDate", lastUpdateDate);
        result.put("gapDays", (int) gapDays);
        result.put("todayWords", todayWords);
        result.put("weekWords", weekWords);
        result.put("monthWords", monthWords);
        result.put("followRead30", followRead30);
        result.put("collect30", collect30);
        result.put("collectTotal", intValue(dashboardMapper.collectTotal(authorId)));
        result.put("draftCount", intValue(draft == null ? null : draft.get("draftCount")));
        result.put("draftWords", intValue(draft == null ? null : draft.get("draftWords")));
        result.put("scheduleCount", intValue(draft == null ? null : draft.get("scheduleCount")));
        return result;
    }

    /**
     * 完整日期轴
     */
    private List<String> buildDateAxis(LocalDate start, int days) {
        List<String> dates = new ArrayList<>(days);
        for (int i = 0; i < days; i++) {
            dates.add(start.plusDays(i).format(DAY_FMT));
        }
        return dates;
    }

    /**
     * 按日期轴对齐统计行，缺失日期补 0
     */
    private List<Object> fillSeries(List<Map<String, Object>> rows, List<String> dates, int days) {
        Map<String, Integer> countMap = toCountMap(rows);
        List<Object> series = new ArrayList<>(days);
        for (String day : dates) {
            series.add(countMap.getOrDefault(day, 0));
        }
        return series;
    }

    private Map<String, Integer> toCountMap(List<Map<String, Object>> rows) {
        Map<String, Integer> countMap = new HashMap<>(rows.size());
        for (Map<String, Object> row : rows) {
            countMap.put(String.valueOf(row.get("d")), intValue(row.get("value")));
        }
        return countMap;
    }

    private int sumValues(Map<String, Integer> map) {
        int sum = 0;
        for (Integer v : map.values()) {
            sum += v;
        }
        return sum;
    }

    private int intValue(Object value) {
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

}
