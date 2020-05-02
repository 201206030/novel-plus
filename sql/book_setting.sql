/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : novel_biz

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2020-05-02 14:49:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book_setting
-- ----------------------------
DROP TABLE IF EXISTS `book_setting`;
CREATE TABLE `book_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `book_id` bigint(20) DEFAULT NULL COMMENT '小说ID',
  `sort` tinyint(4) DEFAULT NULL COMMENT '排序号',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型，0：轮播图，1：顶部小说栏设置，2：本周强推，3：热门推荐，4：精品推荐',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COMMENT='首页小说设置表';

-- ----------------------------
-- Records of book_setting
-- ----------------------------
INSERT INTO `book_setting` VALUES ('32', '1256426898110472192', '1', '0', '2020-04-27 15:45:58', null, '2020-04-27 15:46:03', null);
INSERT INTO `book_setting` VALUES ('33', '1256428364971827200', '2', '0', '2020-04-27 15:46:21', null, '2020-04-27 15:46:24', null);
INSERT INTO `book_setting` VALUES ('34', '1256427853342236672', '3', '0', '2020-04-27 15:47:06', null, '2020-04-27 15:47:09', null);
INSERT INTO `book_setting` VALUES ('35', '1256429556376461312', '4', '0', '2020-04-27 15:47:24', null, '2020-04-27 15:47:27', null);
INSERT INTO `book_setting` VALUES ('36', '1256434212276199424', '1', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('37', '1256427129677996032', '2', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('38', '1256432786909093888', '3', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('39', '1256428080056950784', '4', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('40', '1256474111985532928', '5', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('41', '1256474314285203456', '1', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('42', '1256474119514308608', '2', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('43', '1256473865536618496', '3', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('44', '1256473751132782592', '4', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('45', '1256473412258185216', '5', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('46', '1256473170368479232', '6', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('47', '1256473085719035904', '1', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('48', '1256472696558927872', '2', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('49', '1256474345134309376', '3', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('50', '1256432786909093888', '4', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('51', '1256427853342236672', '5', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('52', '1256474119514308608', '6', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('53', '1256427853342236672', '1', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('54', '1256473751132782592', '2', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('55', '1256428364971827200', '3', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('56', '1256432786909093888', '4', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('57', '1256427129677996032', '5', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('58', '1256474314285203456', '6', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('59', '1256474119514308608', '6', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('60', '1256434330693984256', '7', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('61', '1256471970466185216', '8', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('62', '1256472024757256192', '9', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('63', '1256472346489733120', '10', '1', null, null, null, null);
