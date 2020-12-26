/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : novel_plus

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-06-15 15:06:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawl_single_task
-- ----------------------------
DROP TABLE IF EXISTS `crawl_single_task`;
CREATE TABLE `crawl_single_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` int(11) DEFAULT NULL COMMENT '爬虫源ID',
  `source_name` varchar(50) DEFAULT NULL COMMENT '爬虫源名',
  `source_book_id` varchar(255) DEFAULT NULL COMMENT '源站小说ID',
  `cat_id` int(11) DEFAULT NULL COMMENT '分类ID',
  `book_name` varchar(50) DEFAULT NULL COMMENT '爬取的小说名',
  `author_name` varchar(50) DEFAULT NULL COMMENT '爬取的小说作者名',
  `task_status` tinyint(1) DEFAULT '2' COMMENT '任务状态，0：失败，1：成功，2；未执行',
  `exc_count` tinyint(2) DEFAULT '0' COMMENT '已经执行次数，最多执行5次',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='抓取单本小说任务表';

-- ----------------------------
-- Records of crawl_single_task
-- ----------------------------
INSERT INTO `crawl_single_task` VALUES ('6', '2', '百书斋', '1', '1', '1', '1', '0', '5', '2020-06-15 14:36:07');
INSERT INTO `crawl_single_task` VALUES ('7', '5', '笔趣阁', '108_108291', '1', '衍天志之不朽仙', '白衣少年丶', '1', '1', '2020-06-15 14:46:08');
