package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class AuthorIncomeDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final AuthorIncome authorIncome = new AuthorIncome();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = authorIncome.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = authorIncome.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> authorId = authorIncome.authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> bookId = authorIncome.bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> incomeMonth = authorIncome.incomeMonth;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> preTaxIncome = authorIncome.preTaxIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> afterTaxIncome = authorIncome.afterTaxIncome;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> payStatus = authorIncome.payStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> confirmStatus = authorIncome.confirmStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> detail = authorIncome.detail;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = authorIncome.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class AuthorIncome extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Long> authorId = column("author_id", JDBCType.BIGINT);

        public final SqlColumn<Long> bookId = column("book_id", JDBCType.BIGINT);

        public final SqlColumn<Date> incomeMonth = column("income_month", JDBCType.DATE);

        public final SqlColumn<Long> preTaxIncome = column("pre_tax_income", JDBCType.BIGINT);

        public final SqlColumn<Long> afterTaxIncome = column("after_tax_income", JDBCType.BIGINT);

        public final SqlColumn<Byte> payStatus = column("pay_status", JDBCType.TINYINT);

        public final SqlColumn<Byte> confirmStatus = column("confirm_status", JDBCType.TINYINT);

        public final SqlColumn<String> detail = column("detail", JDBCType.VARCHAR);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public AuthorIncome() {
            super("author_income");
        }
    }
}