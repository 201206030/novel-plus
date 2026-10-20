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
