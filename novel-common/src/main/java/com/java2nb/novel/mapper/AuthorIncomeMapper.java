package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.AuthorIncomeDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.AuthorIncome;
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
public interface AuthorIncomeMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, userId, authorId, bookId, incomeMonth, preTaxIncome, afterTaxIncome, payStatus, confirmStatus, detail, createTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<AuthorIncome> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<AuthorIncome> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("AuthorIncomeResult")
    Optional<AuthorIncome> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="AuthorIncomeResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="author_id", property="authorId", jdbcType=JdbcType.BIGINT),
        @Result(column="book_id", property="bookId", jdbcType=JdbcType.BIGINT),
        @Result(column="income_month", property="incomeMonth", jdbcType=JdbcType.DATE),
        @Result(column="pre_tax_income", property="preTaxIncome", jdbcType=JdbcType.BIGINT),
        @Result(column="after_tax_income", property="afterTaxIncome", jdbcType=JdbcType.BIGINT),
        @Result(column="pay_status", property="payStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="confirm_status", property="confirmStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="detail", property="detail", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<AuthorIncome> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(AuthorIncome record) {
        return MyBatis3Utils.insert(this::insert, record, authorIncome, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(authorId).toProperty("authorId")
            .map(bookId).toProperty("bookId")
            .map(incomeMonth).toProperty("incomeMonth")
            .map(preTaxIncome).toProperty("preTaxIncome")
            .map(afterTaxIncome).toProperty("afterTaxIncome")
            .map(payStatus).toProperty("payStatus")
            .map(confirmStatus).toProperty("confirmStatus")
            .map(detail).toProperty("detail")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<AuthorIncome> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, authorIncome, c ->
            c.map(id).toProperty("id")
            .map(userId).toProperty("userId")
            .map(authorId).toProperty("authorId")
            .map(bookId).toProperty("bookId")
            .map(incomeMonth).toProperty("incomeMonth")
            .map(preTaxIncome).toProperty("preTaxIncome")
            .map(afterTaxIncome).toProperty("afterTaxIncome")
            .map(payStatus).toProperty("payStatus")
            .map(confirmStatus).toProperty("confirmStatus")
            .map(detail).toProperty("detail")
            .map(createTime).toProperty("createTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(AuthorIncome record) {
        return MyBatis3Utils.insert(this::insert, record, authorIncome, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(authorId).toPropertyWhenPresent("authorId", record::getAuthorId)
            .map(bookId).toPropertyWhenPresent("bookId", record::getBookId)
            .map(incomeMonth).toPropertyWhenPresent("incomeMonth", record::getIncomeMonth)
            .map(preTaxIncome).toPropertyWhenPresent("preTaxIncome", record::getPreTaxIncome)
            .map(afterTaxIncome).toPropertyWhenPresent("afterTaxIncome", record::getAfterTaxIncome)
            .map(payStatus).toPropertyWhenPresent("payStatus", record::getPayStatus)
            .map(confirmStatus).toPropertyWhenPresent("confirmStatus", record::getConfirmStatus)
            .map(detail).toPropertyWhenPresent("detail", record::getDetail)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorIncome> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorIncome> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<AuthorIncome> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<AuthorIncome> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, authorIncome, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(AuthorIncome record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(userId).equalTo(record::getUserId)
                .set(authorId).equalTo(record::getAuthorId)
                .set(bookId).equalTo(record::getBookId)
                .set(incomeMonth).equalTo(record::getIncomeMonth)
                .set(preTaxIncome).equalTo(record::getPreTaxIncome)
                .set(afterTaxIncome).equalTo(record::getAfterTaxIncome)
                .set(payStatus).equalTo(record::getPayStatus)
                .set(confirmStatus).equalTo(record::getConfirmStatus)
                .set(detail).equalTo(record::getDetail)
                .set(createTime).equalTo(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(AuthorIncome record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(authorId).equalToWhenPresent(record::getAuthorId)
                .set(bookId).equalToWhenPresent(record::getBookId)
                .set(incomeMonth).equalToWhenPresent(record::getIncomeMonth)
                .set(preTaxIncome).equalToWhenPresent(record::getPreTaxIncome)
                .set(afterTaxIncome).equalToWhenPresent(record::getAfterTaxIncome)
                .set(payStatus).equalToWhenPresent(record::getPayStatus)
                .set(confirmStatus).equalToWhenPresent(record::getConfirmStatus)
                .set(detail).equalToWhenPresent(record::getDetail)
                .set(createTime).equalToWhenPresent(record::getCreateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(AuthorIncome record) {
        return update(c ->
            c.set(userId).equalTo(record::getUserId)
            .set(authorId).equalTo(record::getAuthorId)
            .set(bookId).equalTo(record::getBookId)
            .set(incomeMonth).equalTo(record::getIncomeMonth)
            .set(preTaxIncome).equalTo(record::getPreTaxIncome)
            .set(afterTaxIncome).equalTo(record::getAfterTaxIncome)
            .set(payStatus).equalTo(record::getPayStatus)
            .set(confirmStatus).equalTo(record::getConfirmStatus)
            .set(detail).equalTo(record::getDetail)
            .set(createTime).equalTo(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(AuthorIncome record) {
        return update(c ->
            c.set(userId).equalToWhenPresent(record::getUserId)
            .set(authorId).equalToWhenPresent(record::getAuthorId)
            .set(bookId).equalToWhenPresent(record::getBookId)
            .set(incomeMonth).equalToWhenPresent(record::getIncomeMonth)
            .set(preTaxIncome).equalToWhenPresent(record::getPreTaxIncome)
            .set(afterTaxIncome).equalToWhenPresent(record::getAfterTaxIncome)
            .set(payStatus).equalToWhenPresent(record::getPayStatus)
            .set(confirmStatus).equalToWhenPresent(record::getConfirmStatus)
            .set(detail).equalToWhenPresent(record::getDetail)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}