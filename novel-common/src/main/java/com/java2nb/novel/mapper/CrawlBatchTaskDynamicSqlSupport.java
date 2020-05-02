package com.java2nb.novel.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class CrawlBatchTaskDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final CrawlBatchTask crawlBatchTask = new CrawlBatchTask();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = crawlBatchTask.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> sourceId = crawlBatchTask.sourceId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> crawlCountSuccess = crawlBatchTask.crawlCountSuccess;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> crawlCountTarget = crawlBatchTask.crawlCountTarget;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> taskStatus = crawlBatchTask.taskStatus;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> startTime = crawlBatchTask.startTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> endTime = crawlBatchTask.endTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class CrawlBatchTask extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<Integer> sourceId = column("source_id", JDBCType.INTEGER);

        public final SqlColumn<Integer> crawlCountSuccess = column("crawl_count_success", JDBCType.INTEGER);

        public final SqlColumn<Integer> crawlCountTarget = column("crawl_count_target", JDBCType.INTEGER);

        public final SqlColumn<Byte> taskStatus = column("task_status", JDBCType.TINYINT);

        public final SqlColumn<Date> startTime = column("start_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Date> endTime = column("end_time", JDBCType.TIMESTAMP);

        public CrawlBatchTask() {
            super("crawl_batch_task");
        }
    }
}