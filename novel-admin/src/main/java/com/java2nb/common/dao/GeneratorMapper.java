package com.java2nb.common.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface GeneratorMapper {

    @Select(
        "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables"
            + " where table_schema = 'novel_plus' and table_name like concat('%',#{tableName},'%')")
    List<Map<String, Object>> list(@Param("tableName") String tableName);

    @Select("select count(*) from information_schema.tables where table_schema = 'novel_plus'")
    int count(Map<String, Object> map);

    @Select(
        "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables \r\n"
            + "	where table_schema = 'novel_plus' and table_name = #{tableName}")
    Map<String, String> get(String tableName);

    @Select(
        "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns\r\n"
            + " where table_name = #{tableName} and table_schema = 'novel_plus' order by ordinal_position")
    List<Map<String, String>> listColumns(String tableName);

    @Select(
        "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns\r\n"
            + " where table_name = #{tableName} and table_schema = 'novel_plus' and column_key = 'PRI' limit 1")
    Map<String, String> getPriColumn(String tableName);
}
