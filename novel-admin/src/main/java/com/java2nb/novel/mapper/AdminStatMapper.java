package com.java2nb.novel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 首页统计Mapper（按天分组计数，表名由Service白名单控制）
 *
 * @author Administrator
 */
@Mapper
public interface AdminStatMapper {

    /**
     * 按天统计指定表的新增数量
     *
     * @param table 表名（Service层白名单校验）
     * @param start 起始日期 yyyy-MM-dd
     * @return 每行 {d: 日期, cnt: 数量}
     */
    List<Map<String, Object>> dailyCount(@Param("table") String table, @Param("start") String start);

    /**
     * 表总数（表名由Service层白名单控制）
     *
     * @param table 表名
     * @return 总行数
     */
    long countAll(@Param("table") String table);

    /**
     * 指定表今日新增数（按 create_time）
     *
     * @param table 表名（Service层白名单校验）
     * @param today 今日零点 yyyy-MM-dd 00:00:00
     * @return 今日新增
     */
    long countToday(@Param("table") String table, @Param("today") String today);

    /**
     * 支付成功订单汇总：累计/今日 金额与笔数（今日按 update_time，即支付成功回调更新时间）
     *
     * @param today 今日零点 yyyy-MM-dd 00:00:00
     * @return 单行 {amount, cnt, today_amount, today_cnt}
     */
    Map<String, Object> payStat(@Param("today") String today);

    /**
     * 按天统计支付成功订单的金额与笔数（按 update_time，即支付完成时刻）
     *
     * @param start 起始日期 yyyy-MM-dd 00:00:00
     * @return 每行 {d, amount, cnt}
     */
    List<Map<String, Object>> payDaily(@Param("start") String start);

    /**
     * 上架小说按分类分布
     *
     * @return 每行 {name, value}，按数量降序
     */
    List<Map<String, Object>> categoryDist();

    /**
     * 热门上架小说 TOP N（按点击量）
     *
     * @param limit 条数
     * @return 每行 {bookName, authorName, visits}
     */
    List<Map<String, Object>> hotBooks(@Param("limit") int limit);

    /**
     * 支付成功订单按渠道分布
     *
     * @return 每行 {name, value, amount}
     */
    List<Map<String, Object>> channelDist();

}
