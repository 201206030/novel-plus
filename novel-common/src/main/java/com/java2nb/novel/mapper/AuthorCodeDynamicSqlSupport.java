package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class AuthorCodeDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final AuthorCode authorCode = new AuthorCode();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = authorCode.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> inviteCode = authorCode.inviteCode;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> validityTime = authorCode.validityTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> isUse = authorCode.isUse;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = authorCode.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> createUserId = authorCode.createUserId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class AuthorCode extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> inviteCode = column("invite_code", JDBCType.VARCHAR);

        public final SqlColumn<Date> validityTime = column("validity_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Byte> isUse = column("is_use", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> createUserId = column("create_user_id", JDBCType.BIGINT);

        public AuthorCode() {
            super("author_code");
        }
    }
}