package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.FriendLinkDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.FriendLink;
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
public interface FriendLinkMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, linkName, linkUrl, sort, isOpen, createUserId, createTime, updateUserId, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<FriendLink> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<FriendLink> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("FriendLinkResult")
    Optional<FriendLink> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="FriendLinkResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="link_name", property="linkName", jdbcType=JdbcType.VARCHAR),
        @Result(column="link_url", property="linkUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="sort", property="sort", jdbcType=JdbcType.TINYINT),
        @Result(column="is_open", property="isOpen", jdbcType=JdbcType.TINYINT),
        @Result(column="create_user_id", property="createUserId", jdbcType=JdbcType.BIGINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_user_id", property="updateUserId", jdbcType=JdbcType.BIGINT),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<FriendLink> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Integer id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(FriendLink record) {
        return MyBatis3Utils.insert(this::insert, record, friendLink, c ->
            c.map(id).toProperty("id")
            .map(linkName).toProperty("linkName")
            .map(linkUrl).toProperty("linkUrl")
            .map(sort).toProperty("sort")
            .map(isOpen).toProperty("isOpen")
            .map(createUserId).toProperty("createUserId")
            .map(createTime).toProperty("createTime")
            .map(updateUserId).toProperty("updateUserId")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<FriendLink> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, friendLink, c ->
            c.map(id).toProperty("id")
            .map(linkName).toProperty("linkName")
            .map(linkUrl).toProperty("linkUrl")
            .map(sort).toProperty("sort")
            .map(isOpen).toProperty("isOpen")
            .map(createUserId).toProperty("createUserId")
            .map(createTime).toProperty("createTime")
            .map(updateUserId).toProperty("updateUserId")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(FriendLink record) {
        return MyBatis3Utils.insert(this::insert, record, friendLink, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(linkName).toPropertyWhenPresent("linkName", record::getLinkName)
            .map(linkUrl).toPropertyWhenPresent("linkUrl", record::getLinkUrl)
            .map(sort).toPropertyWhenPresent("sort", record::getSort)
            .map(isOpen).toPropertyWhenPresent("isOpen", record::getIsOpen)
            .map(createUserId).toPropertyWhenPresent("createUserId", record::getCreateUserId)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(updateUserId).toPropertyWhenPresent("updateUserId", record::getUpdateUserId)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<FriendLink> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<FriendLink> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<FriendLink> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<FriendLink> selectByPrimaryKey(Integer id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, friendLink, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(FriendLink record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(linkName).equalTo(record::getLinkName)
                .set(linkUrl).equalTo(record::getLinkUrl)
                .set(sort).equalTo(record::getSort)
                .set(isOpen).equalTo(record::getIsOpen)
                .set(createUserId).equalTo(record::getCreateUserId)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateUserId).equalTo(record::getUpdateUserId)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(FriendLink record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(linkName).equalToWhenPresent(record::getLinkName)
                .set(linkUrl).equalToWhenPresent(record::getLinkUrl)
                .set(sort).equalToWhenPresent(record::getSort)
                .set(isOpen).equalToWhenPresent(record::getIsOpen)
                .set(createUserId).equalToWhenPresent(record::getCreateUserId)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateUserId).equalToWhenPresent(record::getUpdateUserId)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(FriendLink record) {
        return update(c ->
            c.set(linkName).equalTo(record::getLinkName)
            .set(linkUrl).equalTo(record::getLinkUrl)
            .set(sort).equalTo(record::getSort)
            .set(isOpen).equalTo(record::getIsOpen)
            .set(createUserId).equalTo(record::getCreateUserId)
            .set(createTime).equalTo(record::getCreateTime)
            .set(updateUserId).equalTo(record::getUpdateUserId)
            .set(updateTime).equalTo(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(FriendLink record) {
        return update(c ->
            c.set(linkName).equalToWhenPresent(record::getLinkName)
            .set(linkUrl).equalToWhenPresent(record::getLinkUrl)
            .set(sort).equalToWhenPresent(record::getSort)
            .set(isOpen).equalToWhenPresent(record::getIsOpen)
            .set(createUserId).equalToWhenPresent(record::getCreateUserId)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(updateUserId).equalToWhenPresent(record::getUpdateUserId)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}