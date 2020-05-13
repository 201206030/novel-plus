package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.AuthorDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.Author;
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
public interface AuthorMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, userId, inviteCode, penName, telPhone, chatAccount, email, workDirection, status, createTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<Author> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<Author> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthorResult")
    Optional<Author> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthorResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="invite_code", property="inviteCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="pen_name", property="penName", jdbcType=JdbcType.VARCHAR),
        @Result(column="tel_phone", property="telPhone", jdbcType=JdbcType.VARCHAR),
        @Result(column="chat_account", property="chatAccount", jdbcType=JdbcType.VARCHAR),
        @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
        @Result(column="work_direction", property="workDirection", jdbcType=JdbcType.TINYINT),
        @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Author> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(Author record) {
        return MyBatis3Utils.insert(this::insert, record, author, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(inviteCode).toProperty("inviteCode")
            .map(penName).toProperty("penName")
            .map(telPhone).toProperty("telPhone")
            .map(chatAccount).toProperty("chatAccount")
            .map(email).toProperty("email")
            .map(workDirection).toProperty("workDirection")
            .map(status).toProperty("status")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<Author> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, author, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(inviteCode).toProperty("inviteCode")
            .map(penName).toProperty("penName")
            .map(telPhone).toProperty("telPhone")
            .map(chatAccount).toProperty("chatAccount")
            .map(email).toProperty("email")
            .map(workDirection).toProperty("workDirection")
            .map(status).toProperty("status")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(Author record) {
        return MyBatis3Utils.insert(this::insert, record, author, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(inviteCode).toPropertyWhenPresent("inviteCode", record::getInviteCode)
            .map(penName).toPropertyWhenPresent("penName", record::getPenName)
            .map(telPhone).toPropertyWhenPresent("telPhone", record::getTelPhone)
            .map(chatAccount).toPropertyWhenPresent("chatAccount", record::getChatAccount)
            .map(email).toPropertyWhenPresent("email", record::getEmail)
            .map(workDirection).toPropertyWhenPresent("workDirection", record::getWorkDirection)
            .map(status).toPropertyWhenPresent("status", record::getStatus)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<Author> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<Author> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<Author> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<Author> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, author, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(Author record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(userId).equalTo(record::getUserId)
                .set(inviteCode).equalTo(record::getInviteCode)
                .set(penName).equalTo(record::getPenName)
                .set(telPhone).equalTo(record::getTelPhone)
                .set(chatAccount).equalTo(record::getChatAccount)
                .set(email).equalTo(record::getEmail)
                .set(workDirection).equalTo(record::getWorkDirection)
                .set(status).equalTo(record::getStatus)
                .set(createTime).equalTo(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(Author record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(inviteCode).equalToWhenPresent(record::getInviteCode)
                .set(penName).equalToWhenPresent(record::getPenName)
                .set(telPhone).equalToWhenPresent(record::getTelPhone)
                .set(chatAccount).equalToWhenPresent(record::getChatAccount)
                .set(email).equalToWhenPresent(record::getEmail)
                .set(workDirection).equalToWhenPresent(record::getWorkDirection)
                .set(status).equalToWhenPresent(record::getStatus)
                .set(createTime).equalToWhenPresent(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(Author record) {
        return update(c ->
            c.set(userId).equalTo(record::getUserId)
            .set(inviteCode).equalTo(record::getInviteCode)
            .set(penName).equalTo(record::getPenName)
            .set(telPhone).equalTo(record::getTelPhone)
            .set(chatAccount).equalTo(record::getChatAccount)
            .set(email).equalTo(record::getEmail)
            .set(workDirection).equalTo(record::getWorkDirection)
            .set(status).equalTo(record::getStatus)
            .set(createTime).equalTo(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(Author record) {
        return update(c ->
            c.set(userId).equalToWhenPresent(record::getUserId)
            .set(inviteCode).equalToWhenPresent(record::getInviteCode)
            .set(penName).equalToWhenPresent(record::getPenName)
            .set(telPhone).equalToWhenPresent(record::getTelPhone)
            .set(chatAccount).equalToWhenPresent(record::getChatAccount)
            .set(email).equalToWhenPresent(record::getEmail)
            .set(workDirection).equalToWhenPresent(record::getWorkDirection)
            .set(status).equalToWhenPresent(record::getStatus)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}