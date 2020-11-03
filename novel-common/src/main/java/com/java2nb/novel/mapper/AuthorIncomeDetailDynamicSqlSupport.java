package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class AuthorIncomeDetailDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final AuthorIncomeDetail authorIncomeDetail = new AuthorIncomeDetail();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = authorIncomeDetail.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = authorIncomeDetail.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> authorId = authorIncomeDetail.authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> bookId = authorIncomeDetail.bookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> incomeDate = authorIncomeDetail.incomeDate;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> incomeAccount = authorIncomeDetail.incomeAccount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> incomeCount = authorIncomeDetail.incomeCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> incomeNumber = authorIncomeDetail.incomeNumber;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = authorIncomeDetail.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class AuthorIncomeDetail extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Long> authorId = column("author_id", JDBCType.BIGINT);

        public final SqlColumn<Long> bookId = column("book_id", JDBCType.BIGINT);

        public final SqlColumn<Date> incomeDate = column("income_date", JDBCType.DATE);

        public final SqlColumn<Integer> incomeAccount = column("income_account", JDBCType.INTEGER);

        public final SqlColumn<Integer> incomeCount = column("income_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> incomeNumber = column("income_number", JDBCType.INTEGER);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public AuthorIncomeDetail() {
            super("author_income_detail");
        }
    }
}