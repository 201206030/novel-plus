package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class OrderPayDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final OrderPay orderPay = new OrderPay();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = orderPay.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> outTradeNo = orderPay.outTradeNo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> tradeNo = orderPay.tradeNo;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> payChannel = orderPay.payChannel;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> totalAmount = orderPay.totalAmount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = orderPay.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> payStatus = orderPay.payStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = orderPay.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = orderPay.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class OrderPay extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> outTradeNo = column("out_trade_no", JDBCType.BIGINT);

        public final SqlColumn<String> tradeNo = column("trade_no", JDBCType.VARCHAR);

        public final SqlColumn<Byte> payChannel = column("pay_channel", JDBCType.TINYINT);

        public final SqlColumn<Integer> totalAmount = column("total_amount", JDBCType.INTEGER);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Byte> payStatus = column("pay_status", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public OrderPay() {
            super("order_pay");
        }
    }
}