package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.NewsDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.News;
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
public interface NewsMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, catId, catName, sourceName, title, readCount, createTime, createUserId, updateTime, updateUserId, content);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<News> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<News> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("NewsResult")
    Optional<News> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="NewsResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="cat_id", property="catId", jdbcType=JdbcType.INTEGER),
        @Result(column="cat_name", property="catName", jdbcType=JdbcType.VARCHAR),
        @Result(column="source_name", property="sourceName", jdbcType=JdbcType.VARCHAR),
        @Result(column="title", property="title", jdbcType=JdbcType.VARCHAR),
        @Result(column="read_count", property="readCount", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_user_id", property="createUserId", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_user_id", property="updateUserId", jdbcType=JdbcType.BIGINT),
        @Result(column="content", property="content", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<News> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(News record) {
        return MyBatis3Utils.insert(this::insert, record, news, c ->
            c.map(id).toProperty("id")
            .map(catId).toProperty("catId")
            .map(catName).toProperty("catName")
            .map(sourceName).toProperty("sourceName")
            .map(title).toProperty("title")
            .map(readCount).toProperty("readCount")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
            .map(updateTime).toProperty("updateTime")
            .map(updateUserId).toProperty("updateUserId")
            .map(content).toProperty("content")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<News> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, news, c ->
            c.map(id).toProperty("id")
            .map(catId).toProperty("catId")
            .map(catName).toProperty("catName")
            .map(sourceName).toProperty("sourceName")
            .map(title).toProperty("title")
            .map(readCount).toProperty("readCount")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
            .map(updateTime).toProperty("updateTime")
            .map(updateUserId).toProperty("updateUserId")
            .map(content).toProperty("content")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(News record) {
        return MyBatis3Utils.insert(this::insert, record, news, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(catId).toPropertyWhenPresent("catId", record::getCatId)
            .map(catName).toPropertyWhenPresent("catName", record::getCatName)
            .map(sourceName).toPropertyWhenPresent("sourceName", record::getSourceName)
            .map(title).toPropertyWhenPresent("title", record::getTitle)
            .map(readCount).toPropertyWhenPresent("readCount", record::getReadCount)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(createUserId).toPropertyWhenPresent("createUserId", record::getCreateUserId)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
            .map(updateUserId).toPropertyWhenPresent("updateUserId", record::getUpdateUserId)
            .map(content).toPropertyWhenPresent("content", record::getContent)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<News> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<News> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<News> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<News> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, news, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(News record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(catId).equalTo(record::getCatId)
                .set(catName).equalTo(record::getCatName)
                .set(sourceName).equalTo(record::getSourceName)
                .set(title).equalTo(record::getTitle)
                .set(readCount).equalTo(record::getReadCount)
                .set(createTime).equalTo(record::getCreateTime)
                .set(createUserId).equalTo(record::getCreateUserId)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(updateUserId).equalTo(record::getUpdateUserId)
                .set(content).equalTo(record::getContent);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(News record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(catId).equalToWhenPresent(record::getCatId)
                .set(catName).equalToWhenPresent(record::getCatName)
                .set(sourceName).equalToWhenPresent(record::getSourceName)
                .set(title).equalToWhenPresent(record::getTitle)
                .set(readCount).equalToWhenPresent(record::getReadCount)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(createUserId).equalToWhenPresent(record::getCreateUserId)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(updateUserId).equalToWhenPresent(record::getUpdateUserId)
                .set(content).equalToWhenPresent(record::getContent);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(News record) {
        return update(c ->
            c.set(catId).equalTo(record::getCatId)
            .set(catName).equalTo(record::getCatName)
            .set(sourceName).equalTo(record::getSourceName)
            .set(title).equalTo(record::getTitle)
            .set(readCount).equalTo(record::getReadCount)
            .set(createTime).equalTo(record::getCreateTime)
            .set(createUserId).equalTo(record::getCreateUserId)
            .set(updateTime).equalTo(record::getUpdateTime)
            .set(updateUserId).equalTo(record::getUpdateUserId)
            .set(content).equalTo(record::getContent)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(News record) {
        return update(c ->
            c.set(catId).equalToWhenPresent(record::getCatId)
            .set(catName).equalToWhenPresent(record::getCatName)
            .set(sourceName).equalToWhenPresent(record::getSourceName)
            .set(title).equalToWhenPresent(record::getTitle)
            .set(readCount).equalToWhenPresent(record::getReadCount)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(createUserId).equalToWhenPresent(record::getCreateUserId)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .set(updateUserId).equalToWhenPresent(record::getUpdateUserId)
            .set(content).equalToWhenPresent(record::getContent)
            .where(id, isEqualTo(record::getId))
        );
    }
}