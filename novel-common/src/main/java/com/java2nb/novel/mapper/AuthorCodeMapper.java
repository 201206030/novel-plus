package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.AuthorCodeDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.AuthorCode;
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
public interface AuthorCodeMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, inviteCode, validityTime, isUse, createTime, createUserId);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<AuthorCode> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<AuthorCode> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthorCodeResult")
    Optional<AuthorCode> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthorCodeResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="invite_code", property="inviteCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="validity_time", property="validityTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="is_use", property="isUse", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_user_id", property="createUserId", jdbcType=JdbcType.BIGINT)
    })
    List<AuthorCode> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(AuthorCode record) {
        return MyBatis3Utils.insert(this::insert, record, authorCode, c ->
            c.map(id).toProperty("id")
            .map(inviteCode).toProperty("inviteCode")
            .map(validityTime).toProperty("validityTime")
            .map(isUse).toProperty("isUse")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<AuthorCode> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, authorCode, c ->
            c.map(id).toProperty("id")
            .map(inviteCode).toProperty("inviteCode")
            .map(validityTime).toProperty("validityTime")
            .map(isUse).toProperty("isUse")
            .map(createTime).toProperty("createTime")
            .map(createUserId).toProperty("createUserId")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(AuthorCode record) {
        return MyBatis3Utils.insert(this::insert, record, authorCode, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(inviteCode).toPropertyWhenPresent("inviteCode", record::getInviteCode)
            .map(validityTime).toPropertyWhenPresent("validityTime", record::getValidityTime)
            .map(isUse).toPropertyWhenPresent("isUse", record::getIsUse)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(createUserId).toPropertyWhenPresent("createUserId", record::getCreateUserId)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorCode> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorCode> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorCode> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorCode> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authorCode, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthorCode record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(inviteCode).equalTo(record::getInviteCode)
                .set(validityTime).equalTo(record::getValidityTime)
                .set(isUse).equalTo(record::getIsUse)
                .set(createTime).equalTo(record::getCreateTime)
                .set(createUserId).equalTo(record::getCreateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthorCode record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(inviteCode).equalToWhenPresent(record::getInviteCode)
                .set(validityTime).equalToWhenPresent(record::getValidityTime)
                .set(isUse).equalToWhenPresent(record::getIsUse)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(createUserId).equalToWhenPresent(record::getCreateUserId);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(AuthorCode record) {
        return update(c ->
            c.set(inviteCode).equalTo(record::getInviteCode)
            .set(validityTime).equalTo(record::getValidityTime)
            .set(isUse).equalTo(record::getIsUse)
            .set(createTime).equalTo(record::getCreateTime)
            .set(createUserId).equalTo(record::getCreateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(AuthorCode record) {
        return update(c ->
            c.set(inviteCode).equalToWhenPresent(record::getInviteCode)
            .set(validityTime).equalToWhenPresent(record::getValidityTime)
            .set(isUse).equalToWhenPresent(record::getIsUse)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(createUserId).equalToWhenPresent(record::getCreateUserId)
            .where(id, isEqualTo(record::getId))
        );
    }
}