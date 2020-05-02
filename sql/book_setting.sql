/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : novel_biz

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2020-05-03 05:31:00
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
INSERT INTO `book_setting` VALUES ('32', '1256560013650870272', '1', '0', '2020-04-27 15:45:58', null, '2020-04-27 15:46:03', null);
INSERT INTO `book_setting` VALUES ('33', '1256560647057883136', '2', '0', '2020-04-27 15:46:21', null, '2020-04-27 15:46:24', null);
INSERT INTO `book_setting` VALUES ('34', '1256560985525633024', '3', '0', '2020-04-27 15:47:06', null, '2020-04-27 15:47:09', null);
INSERT INTO `book_setting` VALUES ('35', '1256562255468609536', '4', '0', '2020-04-27 15:47:24', null, '2020-04-27 15:47:27', null);
INSERT INTO `book_setting` VALUES ('36', '1256567122404753408', '1', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('37', '1256562643605307392', '2', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('38', '1256567133926506496', '3', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('39', '1256567122404753408', '4', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('40', '1256567622755860480', '5', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('41', '1256567796077084672', '1', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('42', '1256568783873425408', '2', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('43', '1256569365753413632', '3', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('44', '1256568158427201536', '4', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('45', '1256569055395889152', '5', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('46', '1256571983737307136', '6', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('47', '1256572866260811777', '1', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('48', '1256572765551378432', '2', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('49', '1256570746774142976', '3', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('50', '1256570389633351680', '4', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('51', '1256575355232108544', '5', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('52', '1256575330401828864', '6', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('53', '1256575474111266816', '1', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('54', '1256576738597453824', '2', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('55', '1256576787712753664', '3', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('56', '1256579884505808896', '4', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('57', '1256579279494234112', '5', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('58', '1256579164301869056', '6', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('59', '1256692842384842752', '6', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('60', '1256695607790133248', '7', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('61', '1256623094850568192', '8', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('62', '1256626444941836288', '9', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('63', '1256623378670731264', '10', '1', null, null, null, null);
