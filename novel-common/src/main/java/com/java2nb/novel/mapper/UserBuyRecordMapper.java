package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.UserBuyRecordDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.UserBuyRecord;
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
public interface UserBuyRecordMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, userId, bookId, bookName, bookIndexId, bookIndexName, buyAmount, createTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<UserBuyRecord> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<UserBuyRecord> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UserBuyRecordResult")
    Optional<UserBuyRecord> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UserBuyRecordResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_id", property="bookId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_name", property="bookName", jdbcType=JdbcType.VARCHAR),
        @Result(column="book_index_id", property="bookIndexId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_index_name", property="bookIndexName", jdbcType=JdbcType.VARCHAR),
        @Result(column="buy_amount", property="buyAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<UserBuyRecord> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(UserBuyRecord record) {
        return MyBatis3Utils.insert(this::insert, record, userBuyRecord, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(bookId).toProperty("bookId")
            .map(bookName).toProperty("bookName")
            .map(bookIndexId).toProperty("bookIndexId")
            .map(bookIndexName).toProperty("bookIndexName")
            .map(buyAmount).toProperty("buyAmount")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<UserBuyRecord> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, userBuyRecord, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(bookId).toProperty("bookId")
            .map(bookName).toProperty("bookName")
            .map(bookIndexId).toProperty("bookIndexId")
            .map(bookIndexName).toProperty("bookIndexName")
            .map(buyAmount).toProperty("buyAmount")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(UserBuyRecord record) {
        return MyBatis3Utils.insert(this::insert, record, userBuyRecord, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(bookId).toPropertyWhenPresent("bookId", record::getBookId)
            .map(bookName).toPropertyWhenPresent("bookName", record::getBookName)
            .map(bookIndexId).toPropertyWhenPresent("bookIndexId", record::getBookIndexId)
            .map(bookIndexName).toPropertyWhenPresent("bookIndexName", record::getBookIndexName)
            .map(buyAmount).toPropertyWhenPresent("buyAmount", record::getBuyAmount)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserBuyRecord> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserBuyRecord> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserBuyRecord> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserBuyRecord> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, userBuyRecord, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(UserBuyRecord record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(userId).equalTo(record::getUserId)
                .set(bookId).equalTo(record::getBookId)
                .set(bookName).equalTo(record::getBookName)
                .set(bookIndexId).equalTo(record::getBookIndexId)
                .set(bookIndexName).equalTo(record::getBookIndexName)
                .set(buyAmount).equalTo(record::getBuyAmount)
                .set(createTime).equalTo(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UserBuyRecord record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(bookId).equalToWhenPresent(record::getBookId)
                .set(bookName).equalToWhenPresent(record::getBookName)
                .set(bookIndexId).equalToWhenPresent(record::getBookIndexId)
                .set(bookIndexName).equalToWhenPresent(record::getBookIndexName)
                .set(buyAmount).equalToWhenPresent(record::getBuyAmount)
                .set(createTime).equalToWhenPresent(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(UserBuyRecord record) {
        return update(c ->
            c.set(userId).equalTo(record::getUserId)
            .set(bookId).equalTo(record::getBookId)
            .set(bookName).equalTo(record::getBookName)
            .set(bookIndexId).equalTo(record::getBookIndexId)
            .set(bookIndexName).equalTo(record::getBookIndexName)
            .set(buyAmount).equalTo(record::getBuyAmount)
            .set(createTime).equalTo(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(UserBuyRecord record) {
        return update(c ->
            c.set(userId).equalToWhenPresent(record::getUserId)
            .set(bookId).equalToWhenPresent(record::getBookId)
            .set(bookName).equalToWhenPresent(record::getBookName)
            .set(bookIndexId).equalToWhenPresent(record::getBookIndexId)
            .set(bookIndexName).equalToWhenPresent(record::getBookIndexName)
            .set(buyAmount).equalToWhenPresent(record::getBuyAmount)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}