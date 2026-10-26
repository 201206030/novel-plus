ALTER TABLE `crawl_source`
    ADD COLUMN `source_from` varchar(64) NOT NULL DEFAULT '小说精品屋' COMMENT '规则来源：小说精品屋（手工创建）/阅读3.0（导入）等' AFTER `source_name`;
