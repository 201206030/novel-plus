DROP TABLE IF EXISTS `book_comment_reply`;
CREATE TABLE `book_comment_reply`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `comment_id`     bigint(20)   DEFAULT NULL COMMENT '评论ID',
    `reply_content`  varchar(512) DEFAULT NULL COMMENT '回复内容',
    `location` varchar(50) DEFAULT NULL COMMENT '地理位置',
    `audit_status`   tinyint(1)   DEFAULT '0' COMMENT '审核状态，0：待审核，1：审核通过，2：审核不通过',
    `create_time`    datetime     DEFAULT NULL COMMENT '回复用户ID',
    `create_user_id` bigint(20)   DEFAULT NULL COMMENT '回复时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='小说评论回复表';