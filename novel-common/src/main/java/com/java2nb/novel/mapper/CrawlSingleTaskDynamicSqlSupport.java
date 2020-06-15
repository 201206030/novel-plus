package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class CrawlSingleTaskDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final CrawlSingleTask crawlSingleTask = new CrawlSingleTask();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = crawlSingleTask.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> sourceId = crawlSingleTask.sourceId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> sourceName = crawlSingleTask.sourceName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> sourceBookId = crawlSingleTask.sourceBookId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> catId = crawlSingleTask.catId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> bookName = crawlSingleTask.bookName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> authorName = crawlSingleTask.authorName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> taskStatus = crawlSingleTask.taskStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> excCount = crawlSingleTask.excCount;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = crawlSingleTask.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class CrawlSingleTask extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> sourceId = column("source_id", JDBCType.INTEGER);

        public final SqlColumn<String> sourceName = column("source_name", JDBCType.VARCHAR);

        public final SqlColumn<String> sourceBookId = column("source_book_id", JDBCType.VARCHAR);

        public final SqlColumn<Integer> catId = column("cat_id", JDBCType.INTEGER);

        public final SqlColumn<String> bookName = column("book_name", JDBCType.VARCHAR);

        public final SqlColumn<String> authorName = column("author_name", JDBCType.VARCHAR);

        public final SqlColumn<Byte> taskStatus = column("task_status", JDBCType.TINYINT);

        public final SqlColumn<Byte> excCount = column("exc_count", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("create_time", JDBCType.TIMESTAMP);

        public CrawlSingleTask() {
            super("crawl_single_task");
        }
    }
}