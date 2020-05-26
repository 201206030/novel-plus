/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : novel_plus

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-05-26 18:06:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book_content0
-- ----------------------------
DROP TABLE IF EXISTS `book_content0`;
CREATE TABLE `book_content0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1155 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content1
-- ----------------------------
DROP TABLE IF EXISTS `book_content1`;
CREATE TABLE `book_content1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=406 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content2
-- ----------------------------
DROP TABLE IF EXISTS `book_content2`;
CREATE TABLE `book_content2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1222 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content3
-- ----------------------------
DROP TABLE IF EXISTS `book_content3`;
CREATE TABLE `book_content3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=410 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content4
-- ----------------------------
DROP TABLE IF EXISTS `book_content4`;
CREATE TABLE `book_content4` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1188 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content5
-- ----------------------------
DROP TABLE IF EXISTS `book_content5`;
CREATE TABLE `book_content5` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=416 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content6
-- ----------------------------
DROP TABLE IF EXISTS `book_content6`;
CREATE TABLE `book_content6` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1180 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content7
-- ----------------------------
DROP TABLE IF EXISTS `book_content7`;
CREATE TABLE `book_content7` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=404 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content8
-- ----------------------------
DROP TABLE IF EXISTS `book_content8`;
CREATE TABLE `book_content8` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1134 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Table structure for book_content9
-- ----------------------------
DROP TABLE IF EXISTS `book_content9`;
CREATE TABLE `book_content9` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=415 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';
