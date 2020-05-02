package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.CrawlBatchTaskDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.CrawlBatchTask;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface CrawlBatchTaskMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, sourceId, crawlCountSuccess, crawlCountTarget, taskStatus, startTime, endTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<CrawlBatchTask> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<CrawlBatchTask> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("CrawlBatchTaskResult")
    Optional<CrawlBatchTask> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="CrawlBatchTaskResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="source_id", property="sourceId", jdbcType=JdbcType.INTEGER),
        @Result(column="crawl_count_success", property="crawlCountSuccess", jdbcType=JdbcType.INTEGER),
        @Result(column="crawl_count_target", property="crawlCountTarget", jdbcType=JdbcType.INTEGER),
        @Result(column="task_status", property="taskStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<CrawlBatchTask> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(CrawlBatchTask record) {
        return MyBatis3Utils.insert(this::insert, record, crawlBatchTask, c ->
            c.map(id).toProperty("id")
            .map(sourceId).toProperty("sourceId")
            .map(crawlCountSuccess).toProperty("crawlCountSuccess")
            .map(crawlCountTarget).toProperty("crawlCountTarget")
            .map(taskStatus).toProperty("taskStatus")
            .map(startTime).toProperty("startTime")
            .map(endTime).toProperty("endTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<CrawlBatchTask> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, crawlBatchTask, c ->
            c.map(id).toProperty("id")
            .map(sourceId).toProperty("sourceId")
            .map(crawlCountSuccess).toProperty("crawlCountSuccess")
            .map(crawlCountTarget).toProperty("crawlCountTarget")
            .map(taskStatus).toProperty("taskStatus")
            .map(startTime).toProperty("startTime")
            .map(endTime).toProperty("endTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(CrawlBatchTask record) {
        return MyBatis3Utils.insert(this::insert, record, crawlBatchTask, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(sourceId).toPropertyWhenPresent("sourceId", record::getSourceId)
            .map(crawlCountSuccess).toPropertyWhenPresent("crawlCountSuccess", record::getCrawlCountSuccess)
            .map(crawlCountTarget).toPropertyWhenPresent("crawlCountTarget", record::getCrawlCountTarget)
            .map(taskStatus).toPropertyWhenPresent("taskStatus", record::getTaskStatus)
            .map(startTime).toPropertyWhenPresent("startTime", record::getStartTime)
            .map(endTime).toPropertyWhenPresent("endTime", record::getEndTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<CrawlBatchTask> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<CrawlBatchTask> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<CrawlBatchTask> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<CrawlBatchTask> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, crawlBatchTask, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(CrawlBatchTask record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(sourceId).equalTo(record::getSourceId)
                .set(crawlCountSuccess).equalTo(record::getCrawlCountSuccess)
                .set(crawlCountTarget).equalTo(record::getCrawlCountTarget)
                .set(taskStatus).equalTo(record::getTaskStatus)
                .set(startTime).equalTo(record::getStartTime)
                .set(endTime).equalTo(record::getEndTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(CrawlBatchTask record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(sourceId).equalToWhenPresent(record::getSourceId)
                .set(crawlCountSuccess).equalToWhenPresent(record::getCrawlCountSuccess)
                .set(crawlCountTarget).equalToWhenPresent(record::getCrawlCountTarget)
                .set(taskStatus).equalToWhenPresent(record::getTaskStatus)
                .set(startTime).equalToWhenPresent(record::getStartTime)
                .set(endTime).equalToWhenPresent(record::getEndTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(CrawlBatchTask record) {
        return update(c ->
            c.set(sourceId).equalTo(record::getSourceId)
            .set(crawlCountSuccess).equalTo(record::getCrawlCountSuccess)
            .set(crawlCountTarget).equalTo(record::getCrawlCountTarget)
            .set(taskStatus).equalTo(record::getTaskStatus)
            .set(startTime).equalTo(record::getStartTime)
            .set(endTime).equalTo(record::getEndTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(CrawlBatchTask record) {
        return update(c ->
            c.set(sourceId).equalToWhenPresent(record::getSourceId)
            .set(crawlCountSuccess).equalToWhenPresent(record::getCrawlCountSuccess)
            .set(crawlCountTarget).equalToWhenPresent(record::getCrawlCountTarget)
            .set(taskStatus).equalToWhenPresent(record::getTaskStatus)
            .set(startTime).equalToWhenPresent(record::getStartTime)
            .set(endTime).equalToWhenPresent(record::getEndTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}