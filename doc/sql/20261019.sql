CREATE TABLE `user_book_list`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(20)   NOT NULL COMMENT '创建用户ID',
    `list_name`   varchar(50)  NOT NULL COMMENT '书单名',
    `description` varchar(300) DEFAULT NULL COMMENT '书单简介',
    `book_count`  int(11)      NOT NULL DEFAULT 0 COMMENT '书籍数量(冗余)',
    `status`      tinyint(1)   NOT NULL DEFAULT 1 COMMENT '状态：1正常 0已删除',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户书单表';

CREATE TABLE `user_book_list_item`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT,
    `list_id`     bigint(20)   NOT NULL COMMENT '书单ID',
    `book_id`     bigint(20)   NOT NULL COMMENT '书籍ID',
    `remark`      varchar(100) DEFAULT NULL COMMENT '推荐语',
    `create_time` datetime     NOT NULL COMMENT '加入时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_list_book` (`list_id`, `book_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='书单条目表';


CREATE TABLE `book_paragraph_comment`
(
    `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
    `book_id`         bigint(20)     NOT NULL COMMENT '书籍ID',
    `book_index_id`   bigint(20)     NOT NULL COMMENT '章节ID',
    `paragraph_idx`   int(11)        NOT NULL COMMENT '段落序号（前端按正文拆分顺序，从0开始）',
    `root_id`         bigint(20)     NOT NULL DEFAULT 0 COMMENT '所属顶层评论ID，0表示顶层评论',
    `reply_user_name` varchar(50)    DEFAULT NULL COMMENT '被回复人昵称（回复时记录）',
    `reply_count`     int(11)        NOT NULL DEFAULT 0 COMMENT '回复数（冗余，仅顶层评论维护）',
    `comment_content` varchar(500)   NOT NULL COMMENT '评论内容',
    `location`        varchar(50)    DEFAULT NULL COMMENT '数据来源ip所在地',
    `create_user_id`  bigint(20)     NOT NULL COMMENT '创建用户ID',
    `create_time`     datetime       NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_index_paragraph` (`book_index_id`, `paragraph_idx`),
    KEY `idx_root_id` (`root_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='书籍段落评论表（含回复）';

