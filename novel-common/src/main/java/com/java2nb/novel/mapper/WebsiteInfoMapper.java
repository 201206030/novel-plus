package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.WebsiteInfoDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.WebsiteInfo;
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
public interface WebsiteInfoMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, name, domain, keyword, description, qq, logo, logoDark, createTime, createUserId, updateTime, updateUserId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<WebsiteInfo> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<WebsiteInfo> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("WebsiteInfoResult")
    Optional<WebsiteInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="WebsiteInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="domain", property="domain", jdbcType=JdbcType.VARCHAR),
        @Result(column="keyword", property="keyword", jdbcType=JdbcType.VARCHAR),
        @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
        @Result(column="qq", property="qq", jdbcType=JdbcType.VARCHAR),
        @Result(column="logo", property="logo", jdbcType=JdbcType.VARCHAR),
        @Result(column="logo_dark", property="logoDark", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_user_id", property="createUserId", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_user_id", property="updateUserId", jdbcType=JdbcType.BIGINT)
    })
    List<WebsiteInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(WebsiteInfo record) {
        return MyBatis3Utils.insert(this::insert, record, websiteInfo, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(domain).toProperty("domain")
            .map(keyword).toProperty("keyword")
            .map(description).toProperty("description")
            .map(qq).toProperty("qq")
            .map(logo).toProperty("logo")
            .map(logoDark).toProperty("logoDark")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
            .map(updateTime).toProperty("updateTime")
            .map(updateUserId).toProperty("updateUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<WebsiteInfo> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, websiteInfo, c ->
            c.map(id).toProperty("id")
            .map(name).toProperty("name")
            .map(domain).toProperty("domain")
            .map(keyword).toProperty("keyword")
            .map(description).toProperty("description")
            .map(qq).toProperty("qq")
            .map(logo).toProperty("logo")
            .map(logoDark).toProperty("logoDark")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
            .map(updateTime).toProperty("updateTime")
            .map(updateUserId).toProperty("updateUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(WebsiteInfo record) {
        return MyBatis3Utils.insert(this::insert, record, websiteInfo, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(name).toPropertyWhenPresent("name", record::getName)
            .map(domain).toPropertyWhenPresent("domain", record::getDomain)
            .map(keyword).toPropertyWhenPresent("keyword", record::getKeyword)
            .map(description).toPropertyWhenPresent("description", record::getDescription)
            .map(qq).toPropertyWhenPresent("qq", record::getQq)
            .map(logo).toPropertyWhenPresent("logo", record::getLogo)
            .map(logoDark).toPropertyWhenPresent("logoDark", record::getLogoDark)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(createUserId).toPropertyWhenPresent("createUserId", record::getCreateUserId)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
            .map(updateUserId).toPropertyWhenPresent("updateUserId", record::getUpdateUserId)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<WebsiteInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<WebsiteInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<WebsiteInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<WebsiteInfo> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, websiteInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(WebsiteInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(name).equalTo(record::getName)
                .set(domain).equalTo(record::getDomain)
                .set(keyword).equalTo(record::getKeyword)
                .set(description).equalTo(record::getDescription)
                .set(qq).equalTo(record::getQq)
                .set(logo).equalTo(record::getLogo)
                .set(logoDark).equalTo(record::getLogoDark)
                .set(createTime).equalTo(record::getCreateTime)
                .set(createUserId).equalTo(record::getCreateUserId)
                .set(updateTime).equalTo(record::getUpdateTime)
                .set(updateUserId).equalTo(record::getUpdateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(WebsiteInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(name).equalToWhenPresent(record::getName)
                .set(domain).equalToWhenPresent(record::getDomain)
                .set(keyword).equalToWhenPresent(record::getKeyword)
                .set(description).equalToWhenPresent(record::getDescription)
                .set(qq).equalToWhenPresent(record::getQq)
                .set(logo).equalToWhenPresent(record::getLogo)
                .set(logoDark).equalToWhenPresent(record::getLogoDark)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(createUserId).equalToWhenPresent(record::getCreateUserId)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime)
                .set(updateUserId).equalToWhenPresent(record::getUpdateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(WebsiteInfo record) {
        return update(c ->
            c.set(name).equalTo(record::getName)
            .set(domain).equalTo(record::getDomain)
            .set(keyword).equalTo(record::getKeyword)
            .set(description).equalTo(record::getDescription)
            .set(qq).equalTo(record::getQq)
            .set(logo).equalTo(record::getLogo)
            .set(logoDark).equalTo(record::getLogoDark)
            .set(createTime).equalTo(record::getCreateTime)
            .set(createUserId).equalTo(record::getCreateUserId)
            .set(updateTime).equalTo(record::getUpdateTime)
            .set(updateUserId).equalTo(record::getUpdateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(WebsiteInfo record) {
        return update(c ->
            c.set(name).equalToWhenPresent(record::getName)
            .set(domain).equalToWhenPresent(record::getDomain)
            .set(keyword).equalToWhenPresent(record::getKeyword)
            .set(description).equalToWhenPresent(record::getDescription)
            .set(qq).equalToWhenPresent(record::getQq)
            .set(logo).equalToWhenPresent(record::getLogo)
            .set(logoDark).equalToWhenPresent(record::getLogoDark)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(createUserId).equalToWhenPresent(record::getCreateUserId)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .set(updateUserId).equalToWhenPresent(record::getUpdateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }
}