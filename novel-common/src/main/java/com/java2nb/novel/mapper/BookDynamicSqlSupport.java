package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class BookDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final Book book = new Book();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = book.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> workDirection = book.workDirection;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> catId = book.catId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> catName = book.catName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> picUrl = book.picUrl;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> bookName = book.bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> authorId = book.authorId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> authorName = book.authorName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> bookDesc = book.bookDesc;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Float> score = book.score;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> bookStatus = book.bookStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> visitCount = book.visitCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> wordCount = book.wordCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> commentCount = book.commentCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> yesterdayBuy = book.yesterdayBuy;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> lastIndexId = book.lastIndexId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> lastIndexName = book.lastIndexName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> lastIndexUpdateTime = book.lastIndexUpdateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> isVip = book.isVip;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> status = book.status;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> updateTime = book.updateTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = book.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> crawlSourceId = book.crawlSourceId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> crawlBookId = book.crawlBookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> crawlLastTime = book.crawlLastTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> crawlIsStop = book.crawlIsStop;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class Book extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Byte> workDirection = column("work_direction", JDBCType.TINYINT);

        public final SqlColumn<Integer> catId = column("cat_id", JDBCType.INTEGER);

        public final SqlColumn<String> catName = column("cat_name", JDBCType.VARCHAR);

        public final SqlColumn<String> picUrl = column("pic_url", JDBCType.VARCHAR);

        public final SqlColumn<String> bookName = column("book_name", JDBCType.VARCHAR);

        public final SqlColumn<Long> authorId = column("author_id", JDBCType.BIGINT);

        public final SqlColumn<String> authorName = column("author_name", JDBCType.VARCHAR);

        public final SqlColumn<String> bookDesc = column("book_desc", JDBCType.VARCHAR);

        public final SqlColumn<Float> score = column("score", JDBCType.REAL);

        public final SqlColumn<Byte> bookStatus = column("book_status", JDBCType.TINYINT);

        public final SqlColumn<Long> visitCount = column("visit_count", JDBCType.BIGINT);

        public final SqlColumn<Integer> wordCount = column("word_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> commentCount = column("comment_count", JDBCType.INTEGER);

        public final SqlColumn<Integer> yesterdayBuy = column("yesterday_buy", JDBCType.INTEGER);

        public final SqlColumn<Long> lastIndexId = column("last_index_id", JDBCType.BIGINT);

        public final SqlColumn<String> lastIndexName = column("last_index_name", JDBCType.VARCHAR);

        public final SqlColumn<Date> lastIndexUpdateTime = column("last_index_update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Byte> isVip = column("is_vip", JDBCType.TINYINT);

        public final SqlColumn<Byte> status = column("status", JDBCType.TINYINT);

        public final SqlColumn<Date> updateTime = column("update_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Integer> crawlSourceId = column("crawl_source_id", JDBCType.INTEGER);

        public final SqlColumn<String> crawlBookId = column("crawl_book_id", JDBCType.VARCHAR);

        public final SqlColumn<Date> crawlLastTime = column("crawl_last_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Byte> crawlIsStop = column("crawl_is_stop", JDBCType.TINYINT);

        public Book() {
            super("book");
        }
    }
}