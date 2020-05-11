package com.java2nb.novel.mapper;

import static com.java2nb.novel.mapper.OrderPayDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.java2nb.novel.entity.OrderPay;
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
public interface OrderPayMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, outTradeNo, tradeNo, payChannel, totalAmount, userId, payStatus, createTime, updateTime);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<OrderPay> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<OrderPay> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("OrderPayResult")
    Optional<OrderPay> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="OrderPayResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.BIGINT),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="pay_channel", property="payChannel", jdbcType=JdbcType.TINYINT),
        @Result(column="total_amount", property="totalAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="pay_status", property="payStatus", jdbcType=JdbcType.TINYINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<OrderPay> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(OrderPay record) {
        return MyBatis3Utils.insert(this::insert, record, orderPay, c ->
            c.map(id).toProperty("id")
            .map(outTradeNo).toProperty("outTradeNo")
            .map(tradeNo).toProperty("tradeNo")
            .map(payChannel).toProperty("payChannel")
            .map(totalAmount).toProperty("totalAmount")
            .map(userId).toProperty("userId")
            .map(payStatus).toProperty("payStatus")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<OrderPay> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, orderPay, c ->
            c.map(id).toProperty("id")
            .map(outTradeNo).toProperty("outTradeNo")
            .map(tradeNo).toProperty("tradeNo")
            .map(payChannel).toProperty("payChannel")
            .map(totalAmount).toProperty("totalAmount")
            .map(userId).toProperty("userId")
            .map(payStatus).toProperty("payStatus")
            .map(createTime).toProperty("createTime")
            .map(updateTime).toProperty("updateTime")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(OrderPay record) {
        return MyBatis3Utils.insert(this::insert, record, orderPay, c ->
            c.map(id).toPropertyWhenPresent("id", record::getId)
            .map(outTradeNo).toPropertyWhenPresent("outTradeNo", record::getOutTradeNo)
            .map(tradeNo).toPropertyWhenPresent("tradeNo", record::getTradeNo)
            .map(payChannel).toPropertyWhenPresent("payChannel", record::getPayChannel)
            .map(totalAmount).toPropertyWhenPresent("totalAmount", record::getTotalAmount)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(payStatus).toPropertyWhenPresent("payStatus", record::getPayStatus)
            .map(createTime).toPropertyWhenPresent("createTime", record::getCreateTime)
            .map(updateTime).toPropertyWhenPresent("updateTime", record::getUpdateTime)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<OrderPay> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<OrderPay> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<OrderPay> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<OrderPay> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, orderPay, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(OrderPay record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalTo(record::getId)
                .set(outTradeNo).equalTo(record::getOutTradeNo)
                .set(tradeNo).equalTo(record::getTradeNo)
                .set(payChannel).equalTo(record::getPayChannel)
                .set(totalAmount).equalTo(record::getTotalAmount)
                .set(userId).equalTo(record::getUserId)
                .set(payStatus).equalTo(record::getPayStatus)
                .set(createTime).equalTo(record::getCreateTime)
                .set(updateTime).equalTo(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(OrderPay record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(id).equalToWhenPresent(record::getId)
                .set(outTradeNo).equalToWhenPresent(record::getOutTradeNo)
                .set(tradeNo).equalToWhenPresent(record::getTradeNo)
                .set(payChannel).equalToWhenPresent(record::getPayChannel)
                .set(totalAmount).equalToWhenPresent(record::getTotalAmount)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(payStatus).equalToWhenPresent(record::getPayStatus)
                .set(createTime).equalToWhenPresent(record::getCreateTime)
                .set(updateTime).equalToWhenPresent(record::getUpdateTime);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(OrderPay record) {
        return update(c ->
            c.set(outTradeNo).equalTo(record::getOutTradeNo)
            .set(tradeNo).equalTo(record::getTradeNo)
            .set(payChannel).equalTo(record::getPayChannel)
            .set(totalAmount).equalTo(record::getTotalAmount)
            .set(userId).equalTo(record::getUserId)
            .set(payStatus).equalTo(record::getPayStatus)
            .set(createTime).equalTo(record::getCreateTime)
            .set(updateTime).equalTo(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(OrderPay record) {
        return update(c ->
            c.set(outTradeNo).equalToWhenPresent(record::getOutTradeNo)
            .set(tradeNo).equalToWhenPresent(record::getTradeNo)
            .set(payChannel).equalToWhenPresent(record::getPayChannel)
            .set(totalAmount).equalToWhenPresent(record::getTotalAmount)
            .set(userId).equalToWhenPresent(record::getUserId)
            .set(payStatus).equalToWhenPresent(record::getPayStatus)
            .set(createTime).equalToWhenPresent(record::getCreateTime)
            .set(updateTime).equalToWhenPresent(record::getUpdateTime)
            .where(id, isEqualTo(record::getId))
        );
    }
}