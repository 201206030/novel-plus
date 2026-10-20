CREATE TABLE `user_message`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_id`      bigint(20)   NOT NULL COMMENT '接收用户ID',
    `send_user_id` bigint(20)   NOT NULL COMMENT '发送用户ID',
    `msg_type`     tinyint(1)   NOT NULL COMMENT '消息类型：1书评被回复 2段评被回复',
    `book_id`      bigint(20)   DEFAULT NULL COMMENT '书籍ID',
    `book_index_id` bigint(20)  DEFAULT NULL COMMENT '章节ID（段评消息跳转阅读页定位用）',
    `target_id`    bigint(20)   NOT NULL COMMENT '被回复的主评论ID（书评ID或顶层段评ID）',
    `content`      varchar(200) DEFAULT NULL COMMENT '回复内容摘要',
    `read_flag`    tinyint(1)   NOT NULL DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    `create_time`  datetime     NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_read` (`user_id`, `read_flag`),
    KEY `idx_user_time` (`user_id`, `create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户消息表';


CREATE TABLE `ai_creation_plan`
(
    `id`              bigint(20)  NOT NULL AUTO_INCREMENT,
    `author_id`       bigint(20)  NOT NULL COMMENT '作者ID',
    `book_id`         bigint(20)  DEFAULT NULL COMMENT '关联小说ID(新建小说确认方案后回填)',
    `stage`           tinyint(1)  NOT NULL DEFAULT 1 COMMENT '创作阶段：1创作问答中 2方案草稿 3方案已确认 4创作完成',
    `creative_params` text COMMENT '创作问答参数JSON(题材/核心创意/主角/冲突/世界观/视角/主题/读者/章节数/字数等)',
    `plan_content`    longtext COMMENT '创作方案JSON(书名/卖点/简介/世界观/人物档案/总纲/分卷大纲/伏笔/章节规划)',
    `outline_version` int(11)     NOT NULL DEFAULT 0 COMMENT '大纲版本号(每次确认方案递增)',
    `create_time`     datetime    NOT NULL COMMENT '创建时间',
    `update_time`     datetime    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_book` (`book_id`),
    KEY `idx_author` (`author_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI创作方案表';

CREATE TABLE `ai_chapter_plan`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT,
    `plan_id`         bigint(20)   NOT NULL COMMENT '创作方案ID',
    `chapter_no`      int(11)      NOT NULL COMMENT '章节序号(从1开始)',
    `index_name`      varchar(100) DEFAULT NULL COMMENT '章节标题',
    `chapter_outline` text COMMENT '章节规划JSON(主要事件/冲突/人物变化/主线关系/结尾悬念)',
    `status`          tinyint(1)   NOT NULL DEFAULT 0 COMMENT '状态：0待创作 1草稿已生成未保存 2已保存为正式章节 3保存中(事务内瞬时态)',
    `index_id`        bigint(20)   DEFAULT NULL COMMENT '已保存的正式章节ID(book_index.id)',
    `word_count`      int(11)      DEFAULT 0 COMMENT '已保存章节字数',
    `summary`         text COMMENT '已完成章节摘要(创作记忆)',
    `create_time`     datetime     NOT NULL COMMENT '创建时间',
    `update_time`     datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_plan_chapter` (`plan_id`, `chapter_no`),
    KEY `idx_plan_status` (`plan_id`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI章节计划表';

CREATE TABLE `ai_creation_task`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,
    `plan_id`      bigint(20)   NOT NULL COMMENT '创作方案ID',
    `author_id`    bigint(20)   NOT NULL COMMENT '作者ID(权限隔离)',
    `task_type`    tinyint(1)   NOT NULL COMMENT '任务类型：1书名建议 2创作方案 3章节正文 4章节校验修订 5章节摘要 6章节规划增量',
    `chapter_no`   int(11)      NOT NULL DEFAULT 0 COMMENT '目标章节序号(非章节任务为0)',
    `status`       tinyint(1)   NOT NULL DEFAULT 0 COMMENT '状态：0执行中 1成功 2失败',
    `running_flag` tinyint(1)   DEFAULT NULL COMMENT '执行中标记(执行中=1，完成置NULL，配合唯一索引防止同一目标重复提交)',
    `result`       longtext COMMENT '生成结果(结构化任务为JSON，正文任务为纯文本草稿)',
    `error_msg`    varchar(500) DEFAULT NULL COMMENT '最近一次失败原因',
    `create_time`  datetime     NOT NULL COMMENT '创建时间',
    `update_time`  datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_running` (`plan_id`, `task_type`, `chapter_no`, `running_flag`),
    KEY `idx_plan` (`plan_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI创作生成任务表';

CREATE TABLE `ai_author_pref`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT,
    `author_id`    bigint(20) NOT NULL COMMENT '作者ID',
    `pref_content` text COMMENT '全局创作偏好JSON(常用题材/叙事视角/文风/章节长度习惯等)',
    `create_time`  datetime   NOT NULL COMMENT '创建时间',
    `update_time`  datetime   NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_author` (`author_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI创作全局偏好表';

alter table `book` modify column `pic_url` varchar(200) NOT NULL DEFAULT '/images/default.gif' COMMENT '小说封面';

