-- ------------------------------------------------------------
-- 作者后台第一批功能：作家资料页独立维护（功能1）
-- author 表新增头像与简介字段，供 /author/author_info.html 维护
-- ------------------------------------------------------------
alter table `author`
    add column `pic_url` varchar(200) DEFAULT NULL COMMENT '作者头像' after `email`,
    add column `intro`   varchar(500) DEFAULT NULL COMMENT '作者简介' after `pic_url`;
