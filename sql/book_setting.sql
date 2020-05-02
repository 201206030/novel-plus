/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : novel_biz

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2020-05-02 20:34:24
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
INSERT INTO `book_setting` VALUES ('32', '1256508909831536640', '1', '0', '2020-04-27 15:45:58', null, '2020-04-27 15:46:03', null);
INSERT INTO `book_setting` VALUES ('33', '1256509051074723840', '2', '0', '2020-04-27 15:46:21', null, '2020-04-27 15:46:24', null);
INSERT INTO `book_setting` VALUES ('34', '1256509068665634816', '3', '0', '2020-04-27 15:47:06', null, '2020-04-27 15:47:09', null);
INSERT INTO `book_setting` VALUES ('35', '1256509141025767424', '4', '0', '2020-04-27 15:47:24', null, '2020-04-27 15:47:27', null);
INSERT INTO `book_setting` VALUES ('36', '1256509083035320320', '1', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('37', '1256509249888927744', '2', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('38', '1256509443724492800', '3', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('39', '1256509817709608960', '4', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('40', '1256509827104849920', '5', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('41', '1256510337291599872', '1', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('42', '1256510608612737024', '2', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('43', '1256510825391144960', '3', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('44', '1256509671097712640', '4', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('45', '1256509888920502272', '5', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('46', '1256512999282130944', '6', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('47', '1256512853504901120', '1', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('48', '1256512756742307840', '2', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('49', '1256512442035290112', '3', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('50', '1256560985525633024', '4', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('51', '1256560647057883136', '5', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('52', '1256554287595470848', '6', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('53', '1256554246860390400', '1', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('54', '1256554229982511104', '2', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('55', '1256553502908301312', '3', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('56', '1256553373446914048', '4', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('57', '1256553684441972737', '5', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('58', '1256553778306301952', '6', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('59', '1256550223910486016', '6', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('60', '1256550046172659712', '7', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('61', '1256550349009797120', '8', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('62', '1256548828624928768', '9', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('63', '1256508909831536640', '10', '1', null, null, null, null);
