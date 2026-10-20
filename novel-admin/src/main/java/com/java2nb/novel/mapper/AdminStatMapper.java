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

}
