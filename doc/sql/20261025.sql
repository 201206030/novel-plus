alter table `author`
    add column `pic_url` varchar(200) DEFAULT NULL COMMENT '作者头像' after `email`,
    add column `intro`   varchar(500) DEFAULT NULL COMMENT '作者简介' after `pic_url`;

alter table `book_index`
    add column `status`       tinyint(4) NOT NULL DEFAULT '1' COMMENT '发布状态：0草稿，1已发布，2定时发布' after `is_vip`,
    add column `publish_time` datetime   DEFAULT NULL COMMENT '定时发布时间（status=2时有效）' after `status`,
    add index `key_bookId_status` (`book_id`, `status`);