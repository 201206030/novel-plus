package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.AuthorIncomeDetailDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.AuthorIncomeDetail;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;

import org.apache.ibatis.annotations.*;
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
public interface AuthorIncomeDetailMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, userId, authorId, bookId, incomeDate, incomeAccount, incomeCount, incomeNumber, createTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<AuthorIncomeDetail> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<AuthorIncomeDetail> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthorIncomeDetailResult")
    Optional<AuthorIncomeDetail> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthorIncomeDetailResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_id", property="bookId", jdbcType=JdbcType.BIGINT),
        @Result(column="income_date", property="incomeDate", jdbcType=JdbcType.DATE),
        @Result(column="income_account", property="incomeAccount", jdbcType=JdbcType.INTEGER),
        @Result(column="income_count", property="incomeCount", jdbcType=JdbcType.INTEGER),
        @Result(column="income_number", property="incomeNumber", jdbcType=JdbcType.INTEGER),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<AuthorIncomeDetail> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(AuthorIncomeDetail record) {
        return MyBatis3Utils.insert(this::insert, record, authorIncomeDetail, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(authorId).toProperty("authorId")
            .map(bookId).toProperty("bookId")
            .map(incomeDate).toProperty("incomeDate")
            .map(incomeAccount).toProperty("incomeAccount")
            .map(incomeCount).toProperty("incomeCount")
            .map(incomeNumber).toProperty("incomeNumber")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<AuthorIncomeDetail> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, authorIncomeDetail, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(authorId).toProperty("authorId")
            .map(bookId).toProperty("bookId")
            .map(incomeDate).toProperty("incomeDate")
            .map(incomeAccount).toProperty("incomeAccount")
            .map(incomeCount).toProperty("incomeCount")
            .map(incomeNumber).toProperty("incomeNumber")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(AuthorIncomeDetail record) {
        return MyBatis3Utils.insert(this::insert, record, authorIncomeDetail, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(authorId).toPropertyWhenPresent("authorId", record::getAuthorId)
            .map(bookId).toPropertyWhenPresent("bookId", record::getBookId)
            .map(incomeDate).toPropertyWhenPresent("incomeDate", record::getIncomeDate)
            .map(incomeAccount).toPropertyWhenPresent("incomeAccount", record::getIncomeAccount)
            .map(incomeCount).toPropertyWhenPresent("incomeCount", record::getIncomeCount)
            .map(incomeNumber).toPropertyWhenPresent("incomeNumber", record::getIncomeNumber)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorIncomeDetail> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorIncomeDetail> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorIncomeDetail> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorIncomeDetail> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authorIncomeDetail, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthorIncomeDetail record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(userId).equalTo(record::getUserId)
                .set(authorId).equalTo(record::getAuthorId)
                .set(bookId).equalTo(record::getBookId)
                .set(incomeDate).equalTo(record::getIncomeDate)
                .set(incomeAccount).equalTo(record::getIncomeAccount)
                .set(incomeCount).equalTo(record::getIncomeCount)
                .set(incomeNumber).equalTo(record::getIncomeNumber)
                .set(createTime).equalTo(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthorIncomeDetail record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(authorId).equalToWhenPresent(record::getAuthorId)
                .set(bookId).equalToWhenPresent(record::getBookId)
                .set(incomeDate).equalToWhenPresent(record::getIncomeDate)
                .set(incomeAccount).equalToWhenPresent(record::getIncomeAccount)
                .set(incomeCount).equalToWhenPresent(record::getIncomeCount)
                .set(incomeNumber).equalToWhenPresent(record::getIncomeNumber)
                .set(createTime).equalToWhenPresent(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(AuthorIncomeDetail record) {
        return update(c ->
            c.set(userId).equalTo(record::getUserId)
            .set(authorId).equalTo(record::getAuthorId)
            .set(bookId).equalTo(record::getBookId)
            .set(incomeDate).equalTo(record::getIncomeDate)
            .set(incomeAccount).equalTo(record::getIncomeAccount)
            .set(incomeCount).equalTo(record::getIncomeCount)
            .set(incomeNumber).equalTo(record::getIncomeNumber)
            .set(createTime).equalTo(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(AuthorIncomeDetail record) {
        return update(c ->
            c.set(userId).equalToWhenPresent(record::getUserId)
            .set(authorId).equalToWhenPresent(record::getAuthorId)
            .set(bookId).equalToWhenPresent(record::getBookId)
            .set(incomeDate).equalToWhenPresent(record::getIncomeDate)
            .set(incomeAccount).equalToWhenPresent(record::getIncomeAccount)
            .set(incomeCount).equalToWhenPresent(record::getIncomeCount)
            .set(incomeNumber).equalToWhenPresent(record::getIncomeNumber)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultType(value = Long.class)
    Long selectStatistic(SelectStatementProvider selectStatement);
}