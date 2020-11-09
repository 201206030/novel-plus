alter table book add column `yesterday_buy` int(11) DEFAULT '0' COMMENT '昨日订阅数' after comment_count;
