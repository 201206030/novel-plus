/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : novel_plus

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-05-11 17:56:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_pay
-- ----------------------------
DROP TABLE IF EXISTS `order_pay`;
CREATE TABLE `order_pay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `out_trade_no` bigint(20) NOT NULL COMMENT '商户订单号',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '支付宝/微信交易号',
  `pay_channel` tinyint(1) NOT NULL DEFAULT '1' COMMENT '支付渠道，1：支付宝，2：微信',
  `total_amount` int(11) NOT NULL COMMENT '交易金额(单位元)',
  `user_id` bigint(20) NOT NULL COMMENT '支付用户ID',
  `pay_status` tinyint(1) DEFAULT '2' COMMENT '支付状态：0：支付失败，1：支付成功，2：待支付',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='充值订单';
