CREATE TABLE `author_income_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `author_id` bigint(20) NOT NULL COMMENT '作家ID',
  `book_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '作品ID,0表示全部作品',
  `income_date` date NOT NULL COMMENT '收入日期',
  `income_account` int(11) NOT NULL DEFAULT '0' COMMENT '订阅总额',
  `income_count` int(11) NOT NULL DEFAULT '0' COMMENT '订阅次数',
  `income_number` int(11) NOT NULL DEFAULT '0' COMMENT '订阅人数',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='稿费收入明细统计表';

CREATE TABLE `author_income` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `author_id` bigint(20) NOT NULL COMMENT '作家ID',
  `book_id` bigint(20) NOT NULL COMMENT '作品ID',
  `income_month` date NOT NULL COMMENT '收入月份',
  `pre_tax_income` bigint(20) NOT NULL DEFAULT '0' COMMENT '税前收入（分）',
  `after_tax_income` bigint(20) NOT NULL DEFAULT '0' COMMENT '税后收入（分）',
  `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态，0：待支付，1：已支付',
  `confirm_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '稿费确认状态，0：待确认，1：已确认',
  `detail` varchar(255) DEFAULT NULL COMMENT '详情',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4  COMMENT='稿费收入统计表';