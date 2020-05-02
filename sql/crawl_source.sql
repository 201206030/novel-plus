/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : novel_biz

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2020-05-02 12:40:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawl_source
-- ----------------------------
DROP TABLE IF EXISTS `crawl_source`;
CREATE TABLE `crawl_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_name` varchar(50) DEFAULT NULL COMMENT '源站名',
  `crawl_rule` varchar(2048) DEFAULT NULL COMMENT '爬取规则（json串）',
  `source_status` tinyint(1) DEFAULT '0' COMMENT '爬虫源状态，0：关闭，1：开启',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='爬虫源表';

-- ----------------------------
-- Records of crawl_source
-- ----------------------------
INSERT INTO `crawl_source` VALUES ('2', '百书斋', '{\r\n	\"bookListUrl\": \"https://m.baishuzhai.com/blhb/{catId}/{page}.html\",\r\n	\"catIdRule\": {\r\n		\"catId1\": \"1\",\r\n		\"catId2\": \"2\",\r\n		\"catId3\": \"3\",\r\n		\"catId4\": \"4\",\r\n		\"catId5\": \"5\",\r\n		\"catId6\": \"6\",\r\n		\"catId7\": \"7\"\r\n	},\r\n	\"bookIdPatten\": \"href=\\\"/ibook/(\\\\d+/\\\\d+)/\\\"\",\r\n	\"pagePatten\": \"value=\\\"(\\\\d+)/\\\\d+\\\"\",\r\n	\"totalPagePatten\": \"value=\\\"\\\\d+/(\\\\d+)\\\"\",\r\n	\"bookDetailUrl\": \"https://m.baishuzhai.com/ibook/{bookId}/\",\r\n	\"bookNamePatten\": \"<span class=\\\"title\\\">([^/]+)</span>\",\r\n	\"authorNamePatten\": \">作者：([^/]+)<\",\r\n	\"picUrlPatten\": \"<img src=\\\"([^>]+)\\\"\\\\s+onerror=\\\"this.src=\",\r\n	\"statusPatten\": \"状态：([^/]+)</li>\",\r\n	\"bookStatusRule\": {\r\n		\"连载\": 0,\r\n		\"完成\": 1\r\n	},\r\n	\"scorePatten\": \"<div\\\\s+class=\\\"score\\\">(\\\\d+\\\\.\\\\d+)分</div>\",\r\n	\"descStart\": \"<p class=\\\"review\\\">\",\r\n	\"descEnd\": \"</p>\",\r\n	\"upadateTimePatten\": \"更新：(\\\\d+-\\\\d+-\\\\d+)</li>\",\r\n	\"upadateTimeFormatPatten\": \"yy-MM-dd\",\r\n	\"bookIndexUrl\": \"https://m.baishuzhai.com/ibook/{bookId}/all.html\",\r\n	\"indexIdPatten\": \"<a\\\\s+style=\\\"\\\"\\\\s+href=\\\"/ibook/\\\\d+/\\\\d+/(\\\\d+)\\\\.html\\\">[^/]+</a>\",\r\n	\"indexNamePatten\": \"<a\\\\s+style=\\\"\\\"\\\\s+href=\\\"/ibook/\\\\d+/\\\\d+/\\\\d+\\\\.html\\\">([^/]+)</a>\",\r\n	\"bookContentUrl\": \"https://baishuzhai.com/ibook/{bookId}/{indexId}.html\",\r\n	\"contentStart\": \"id=\\\"content\\\">\",\r\n	\"contentEnd\": \"<script>\"\r\n}', '1', '2020-05-01 14:22:50', '2020-05-01 14:22:50');
