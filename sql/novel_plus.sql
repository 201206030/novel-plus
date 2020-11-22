/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : novel_plus

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-05-18 13:59:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for author
-- ----------------------------
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `invite_code` varchar(20) DEFAULT NULL COMMENT '邀请码',
  `pen_name` varchar(20) DEFAULT NULL COMMENT '笔名',
  `tel_phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `chat_account` varchar(50) DEFAULT NULL COMMENT 'QQ或微信账号',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `work_direction` tinyint(4) DEFAULT NULL COMMENT '作品方向，0：男频，1：女频',
  `status` tinyint(4) DEFAULT '0' COMMENT '0：正常，1：封禁',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='作者表';

-- ----------------------------
-- Records of author
-- ----------------------------
INSERT INTO `author` VALUES ('1', null, 'reerer', 'abc', '13560487656', '23484388', '23484388@qq.com', '0', '0', null);
INSERT INTO `author` VALUES ('2', '1255060328322027520', 'rwrr445554', '梦入神机', '13560421324', '1179705413', 'reerer@qq.com', '0', '0', '2020-05-13 14:01:31');

-- ----------------------------
-- Table structure for author_code
-- ----------------------------
DROP TABLE IF EXISTS `author_code`;
CREATE TABLE `author_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invite_code` varchar(100) DEFAULT NULL COMMENT '邀请码',
  `validity_time` datetime DEFAULT NULL COMMENT '有效时间',
  `is_use` tinyint(1) DEFAULT '0' COMMENT '是否使用过，0：未使用，1:使用过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_code` (`invite_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='作家邀请码表';

-- ----------------------------
-- Records of author_code
-- ----------------------------
INSERT INTO `author_code` VALUES ('3', 'reerer', '2020-05-27 22:43:45', '1', '2020-05-13 11:40:56', '1');
INSERT INTO `author_code` VALUES ('4', '123456', '2020-05-28 00:00:00', '0', '2020-05-13 14:09:55', '1');
INSERT INTO `author_code` VALUES ('5', 'ww34343', '2020-05-21 00:00:00', '0', '2020-05-13 14:18:58', '1');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_direction` tinyint(1) DEFAULT NULL COMMENT '作品方向，0：男频，1：女频''',
  `cat_id` int(11) DEFAULT NULL COMMENT '分类ID',
  `cat_name` varchar(50) DEFAULT NULL COMMENT '分类名',
  `pic_url` varchar(200) NOT NULL COMMENT '小说封面',
  `book_name` varchar(50) NOT NULL COMMENT '小说名',
  `author_id` bigint(20) DEFAULT NULL COMMENT '作者id',
  `author_name` varchar(50) NOT NULL COMMENT '作者名',
  `book_desc` varchar(2000) NOT NULL COMMENT '书籍描述',
  `score` float NOT NULL COMMENT '评分，预留字段',
  `book_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '书籍状态，0：连载中，1：已完结',
  `visit_count` bigint(20) DEFAULT '103' COMMENT '点击量',
  `word_count` int(11) DEFAULT NULL COMMENT '总字数',
  `comment_count` int(11) DEFAULT '0' COMMENT '评论数',
  `last_index_id` bigint(20) DEFAULT NULL COMMENT '最新目录ID',
  `last_index_name` varchar(50) DEFAULT NULL COMMENT '最新目录名',
  `last_index_update_time` datetime DEFAULT NULL COMMENT '最新目录更新时间',
  `is_vip` tinyint(1) DEFAULT '0' COMMENT '是否收费，1：收费，0：免费',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态，0：入库，1：上架',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `crawl_source_id` int(11) DEFAULT NULL COMMENT '爬虫源站ID',
  `crawl_book_id` varchar(32) DEFAULT NULL COMMENT '抓取的源站小说ID',
  `crawl_last_time` datetime DEFAULT NULL COMMENT '最后一次的抓取时间',
  `crawl_is_stop` tinyint(1) DEFAULT '0' COMMENT '是否已停止更新，0：未停止，1：已停止',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_bookName_authorName` (`book_name`,`author_name`) USING BTREE,
  KEY `key_lastIndexUpdateTime` (`last_index_update_time`) USING BTREE,
  KEY `key_createTime` (`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1262260513468559361 DEFAULT CHARSET=utf8mb4 COMMENT='小说表';

-- ----------------------------
-- Records of book
-- ----------------------------

-- ----------------------------
-- Table structure for book_author
-- ----------------------------
DROP TABLE IF EXISTS `book_author`;
CREATE TABLE `book_author` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `invite_code` varchar(20) DEFAULT NULL COMMENT '邀请码',
  `pen_name` varchar(20) DEFAULT NULL COMMENT '笔名',
  `tel_phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `chat_account` varchar(50) DEFAULT NULL COMMENT 'QQ或微信账号',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `work_direction` tinyint(4) DEFAULT NULL COMMENT '作品方向，0：男频，1：女频',
  `status` tinyint(4) DEFAULT NULL COMMENT '0：待审核，1：审核通过，正常，2：审核不通过',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '申请人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1254957873655066625 DEFAULT CHARSET=utf8mb4 COMMENT='作者表';

-- ----------------------------
-- Records of book_author
-- ----------------------------

-- ----------------------------
-- Table structure for book_category
-- ----------------------------
DROP TABLE IF EXISTS `book_category`;
CREATE TABLE `book_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_direction` tinyint(1) DEFAULT NULL COMMENT '作品方向，0：男频，1：女频''',
  `name` varchar(20) NOT NULL COMMENT '分类名',
  `sort` tinyint(4) NOT NULL DEFAULT '10' COMMENT '排序',
  `create_user_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='小说类别表';

-- ----------------------------
-- Records of book_category
-- ----------------------------
INSERT INTO `book_category` VALUES ('1', '0', '玄幻奇幻', '10', null, null, null, null);
INSERT INTO `book_category` VALUES ('2', '0', '武侠仙侠', '11', null, null, null, null);
INSERT INTO `book_category` VALUES ('3', '0', '都市言情', '12', null, null, null, null);
INSERT INTO `book_category` VALUES ('4', '0', '历史军事', '13', null, null, null, null);
INSERT INTO `book_category` VALUES ('5', '0', '科幻灵异', '14', null, null, null, null);
INSERT INTO `book_category` VALUES ('6', '0', '网游竞技', '15', null, null, null, null);
INSERT INTO `book_category` VALUES ('7', '1', '女生频道', '16', null, null, null, null);

-- ----------------------------
-- Table structure for book_comment
-- ----------------------------
DROP TABLE IF EXISTS `book_comment`;
CREATE TABLE `book_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_id` bigint(20) DEFAULT NULL COMMENT '小说ID',
  `comment_content` varchar(512) DEFAULT NULL COMMENT '评价内容',
  `reply_count` int(11) DEFAULT '0' COMMENT '回复数量',
  `audit_status` tinyint(1) DEFAULT '0' COMMENT '审核状态，0：待审核，1：审核通过，2：审核不通过',
  `create_time` datetime DEFAULT NULL COMMENT '评价时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '评价人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_bookid_userid` (`book_id`,`create_user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='小说评论表';

-- ----------------------------
-- Records of book_comment
-- ----------------------------
INSERT INTO `book_comment` VALUES ('11', '1254678892443795456', '好书呀，值得一看', '0', '0', '2020-04-28 17:04:56', '1255060328322027520');
INSERT INTO `book_comment` VALUES ('12', '1254954626689150976', 'ffgfgfffffffffff', '0', '0', '2020-04-30 08:35:53', '1255060328322027520');

-- ----------------------------
-- Table structure for book_comment_reply
-- ----------------------------
DROP TABLE IF EXISTS `book_comment_reply`;
CREATE TABLE `book_comment_reply` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `comment_id` bigint(20) DEFAULT NULL COMMENT '评论ID',
  `reply_content` varchar(512) DEFAULT NULL COMMENT '回复内容',
  `audit_status` tinyint(1) DEFAULT '0' COMMENT '审核状态，0：待审核，1：审核通过，2：审核不通过',
  `create_time` datetime DEFAULT NULL COMMENT '回复用户ID',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小说评论回复表';

-- ----------------------------
-- Records of book_comment_reply
-- ----------------------------

-- ----------------------------
-- Table structure for book_content
-- ----------------------------
DROP TABLE IF EXISTS `book_content`;
CREATE TABLE `book_content` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `index_id` bigint(20) DEFAULT NULL COMMENT '目录ID',
  `content` mediumtext COMMENT '小说章节内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_indexId` (`index_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3347665 DEFAULT CHARSET=utf8mb4 COMMENT='小说内容表';

-- ----------------------------
-- Records of book_content
-- ----------------------------

-- ----------------------------
-- Table structure for book_index
-- ----------------------------
DROP TABLE IF EXISTS `book_index`;
CREATE TABLE `book_index` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `book_id` bigint(20) NOT NULL COMMENT '小说ID',
  `index_num` int(11) NOT NULL COMMENT '目录号',
  `index_name` varchar(100) DEFAULT NULL COMMENT '目录名',
  `word_count` int(11) DEFAULT NULL COMMENT '字数',
  `is_vip` tinyint(4) DEFAULT '0' COMMENT '是否收费，1：收费，0：免费',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_bookId_indexNum` (`book_id`,`index_num`) USING BTREE,
  KEY `key_bookId` (`book_id`) USING BTREE,
  KEY `key_indexNum` (`index_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1262260612777095169 DEFAULT CHARSET=utf8mb4 COMMENT='小说目录表';

-- ----------------------------
-- Records of book_index
-- ----------------------------

-- ----------------------------
-- Table structure for book_screen_bullet
-- ----------------------------
DROP TABLE IF EXISTS `book_screen_bullet`;
CREATE TABLE `book_screen_bullet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content_id` bigint(20) NOT NULL COMMENT '小说内容ID',
  `screen_bullet` varchar(512) NOT NULL COMMENT '小说弹幕内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `key_contentId` (`content_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COMMENT='小说弹幕表';

-- ----------------------------
-- Records of book_screen_bullet
-- ----------------------------

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
INSERT INTO `book_setting` VALUES ('32', '1254674114481422336', '1', '0', '2020-04-27 15:45:58', null, '2020-04-27 15:46:03', null);
INSERT INTO `book_setting` VALUES ('33', '1254674171310047232', '2', '0', '2020-04-27 15:46:21', null, '2020-04-27 15:46:24', null);
INSERT INTO `book_setting` VALUES ('34', '1254674255883993088', '3', '0', '2020-04-27 15:47:06', null, '2020-04-27 15:47:09', null);
INSERT INTO `book_setting` VALUES ('35', '1254674396451897344', '4', '0', '2020-04-27 15:47:24', null, '2020-04-27 15:47:27', null);
INSERT INTO `book_setting` VALUES ('36', '1254674613117059072', '1', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('37', '1254680030366859264', '2', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('38', '1254677251162308608', '3', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('39', '1254677745226153984', '4', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('40', '1254677887534694400', '5', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('41', '1254675594315759616', '1', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('42', '1254675739140882432', '2', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('43', '1254675826696978432', '3', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('44', '1254676309448785920', '4', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('45', '1254676443012202496', '5', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('46', '1254676564366000128', '6', '2', null, null, null, null);
INSERT INTO `book_setting` VALUES ('47', '1254676970567565312', '1', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('48', '1254677251162308608', '2', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('49', '1254677745226153984', '3', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('50', '1254677887534694400', '4', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('51', '1254675826696978432', '5', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('52', '1254676970567565312', '6', '3', null, null, null, null);
INSERT INTO `book_setting` VALUES ('53', '1254681827219275776', '1', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('54', '1254681178427555840', '2', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('55', '1254681827219275776', '3', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('56', '1254681753466634240', '4', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('57', '1254682148440047616', '5', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('58', '1254682422076440576', '6', '4', null, null, null, null);
INSERT INTO `book_setting` VALUES ('59', '1254674794009001984', '6', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('60', '1254678892443795456', '7', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('61', '1254681753466634240', '8', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('62', '1254681071191785472', '9', '1', null, null, null, null);
INSERT INTO `book_setting` VALUES ('63', '1254677745226153984', '10', '1', null, null, null, null);

-- ----------------------------
-- Table structure for crawl_batch_task
-- ----------------------------
DROP TABLE IF EXISTS `crawl_batch_task`;
CREATE TABLE `crawl_batch_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` int(11) DEFAULT NULL COMMENT '爬虫源ID',
  `crawl_count_success` int(11) DEFAULT NULL COMMENT '成功抓取数量',
  `crawl_count_target` int(11) DEFAULT NULL COMMENT '目标抓取数量',
  `task_status` tinyint(1) DEFAULT '1' COMMENT '任务状态，1：正在运行，0已停止',
  `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批量抓取任务表';

-- ----------------------------
-- Records of crawl_batch_task
-- ----------------------------

-- ----------------------------
-- Table structure for crawl_single_task
-- ----------------------------
DROP TABLE IF EXISTS `crawl_single_task`;
CREATE TABLE `crawl_single_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_id` int(11) DEFAULT NULL COMMENT '爬虫源ID',
  `source_book_id` varchar(255) DEFAULT NULL COMMENT '源站小说ID',
  `task_status` tinyint(1) DEFAULT NULL COMMENT '任务状态，0：失败，1：成功，2；未执行',
  `exc_count` tinyint(2) DEFAULT '0' COMMENT '已经执行次数，最多执行5次',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抓取单本小说任务表';

-- ----------------------------
-- Records of crawl_single_task
-- ----------------------------

-- ----------------------------
-- Table structure for crawl_source
-- ----------------------------
DROP TABLE IF EXISTS `crawl_source`;
CREATE TABLE `crawl_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `source_name` varchar(50) DEFAULT NULL COMMENT '源站名',
  `crawl_rule` text COMMENT '爬取规则（json串）',
  `source_status` tinyint(1) DEFAULT '0' COMMENT '爬虫源状态，0：关闭，1：开启',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='爬虫源表';

-- ----------------------------
-- Records of crawl_source
-- ----------------------------
INSERT INTO `crawl_source` VALUES ('2', '百书斋', '{\r\n	\"bookListUrl\": \"https://m.baishuzhai.com/blhb/{catId}/{page}.html\",\r\n	\"catIdRule\": {\r\n		\"catId1\": \"1\",\r\n		\"catId2\": \"2\",\r\n		\"catId3\": \"3\",\r\n		\"catId4\": \"4\",\r\n		\"catId5\": \"5\",\r\n		\"catId6\": \"6\",\r\n		\"catId7\": \"7\"\r\n	},\r\n	\"bookIdPatten\": \"href=\\\"/ibook/(\\\\d+/\\\\d+)/\\\"\",\r\n	\"pagePatten\": \"value=\\\"(\\\\d+)/\\\\d+\\\"\",\r\n	\"totalPagePatten\": \"value=\\\"\\\\d+/(\\\\d+)\\\"\",\r\n	\"bookDetailUrl\": \"https://m.baishuzhai.com/ibook/{bookId}/\",\r\n	\"bookNamePatten\": \"<span class=\\\"title\\\">([^/]+)</span>\",\r\n	\"authorNamePatten\": \">作者：([^/]+)<\",\r\n	\"picUrlPatten\": \"<img src=\\\"([^>]+)\\\"\\\\s+onerror=\\\"this.src=\",\r\n	\"statusPatten\": \"状态：([^/]+)</li>\",\r\n	\"bookStatusRule\": {\r\n		\"连载\": 0,\r\n		\"完成\": 1\r\n	},\r\n	\"scorePatten\": \"<em>([^<]+)</em>\",\r\n	\"descStart\": \"<p class=\\\"review\\\">\",\r\n	\"descEnd\": \"</p>\",\r\n	\"upadateTimePatten\": \"更新：(\\\\d+-\\\\d+-\\\\d+)</li>\",\r\n	\"upadateTimeFormatPatten\": \"yy-MM-dd\",\r\n	\"bookIndexUrl\": \"https://m.baishuzhai.com/ibook/{bookId}/all.html\",\r\n	\"indexIdPatten\": \"<a\\\\s+style=\\\"\\\"\\\\s+href=\\\"/ibook/\\\\d+/\\\\d+/(\\\\d+)\\\\.html\\\">[^/]+</a>\",\r\n	\"indexNamePatten\": \"<a\\\\s+style=\\\"\\\"\\\\s+href=\\\"/ibook/\\\\d+/\\\\d+/\\\\d+\\\\.html\\\">([^/]+)</a>\",\r\n	\"bookContentUrl\": \"https://baishuzhai.com/ibook/{bookId}/{indexId}.html\",\r\n	\"contentStart\": \"id=\\\"content\\\">\",\r\n	\"contentEnd\": \"<script>\"\r\n}', '0', '2020-05-01 14:22:50', '2020-05-01 14:22:50');
INSERT INTO `crawl_source` VALUES ('3', '书包网', '{\r\n	\"bookListUrl\": \"https://www.bookbao8.com/booklist-p_{page}-c_{catId}-t_0-o_0.html\",\r\n	\"catIdRule\": {\r\n		\"catId1\": \"5\",\r\n		\"catId2\": \"4\",\r\n		\"catId3\": \"8\",\r\n		\"catId4\": \"9\",\r\n		\"catId5\": \"3\",\r\n		\"catId6\": \"7\"\r\n	},\r\n	\"bookIdPatten\": \"href=\\\"/book/(\\\\d+/\\\\d+/id_[^.]+).html\\\"\",\r\n	\"pagePatten\": \"<span\\\\s+class=\\\"current\\\">([^<]+)</span>\",\r\n	\"totalPagePatten\": \"/共(\\\\d+)页\",\r\n	\"bookDetailUrl\": \"https://www.bookbao8.com/book/{bookId}.html\",\r\n	\"bookNamePatten\": \"<div\\\\s+id=\\\"info\\\">\\\\s*<h1>([^<]+)</h1>\",\r\n	\"authorNamePatten\": \"<p>作者：<a\\\\s+href=\\\"/Search/[^\\\"]+\\\"\\\\s+target=\\\"_blank\\\">([^<]+)</a></p>\",\r\n	\"picUrlPatten\": \"<div\\\\s+id=\\\"fmimg\\\">\\\\s*<img\\\\s+alt=\\\"[^\\\"]+\\\"\\\\s+src=\\\"([^\\\"]+)\\\"\",\r\n	\"statusPatten\": \"<p>状态：([^<]+)</p>\",\r\n	\"bookStatusRule\": {\r\n		\"连载中\": 0,\r\n		\"已完结\": 1\r\n	},\r\n	\"visitCountPatten\": \"<em\\\\s+id=\\\"hits\\\">(\\\\d+)</em>\",\r\n	\"descStart\": \"<div class=\\\"infocontent\\\">\",\r\n	\"descEnd\": \"</div>\",\r\n	\"upadateTimePatten\": \"<p>更新时间：(\\\\d+-\\\\d+-\\\\d+\\\\s\\\\d+:\\\\d+:\\\\d+)</p>\",\r\n	\"upadateTimeFormatPatten\": \"yyyy-MM-dd HH:mm:ss\",\r\n	\"bookIndexUrl\": \"https://www.bookbao8.com/book/{bookId}.html\",\r\n	\"indexIdPatten\": \"<li>\\\\s*<a\\\\s+href=\\\"/views/\\\\d+/\\\\d+/id_[^_]+_(\\\\d+).html\\\"\\\\s+target=\\\"_blank\\\">\",\r\n	\"indexNamePatten\": \"<li>\\\\s*<a\\\\s+href=\\\"/views/\\\\d+/\\\\d+/id_[^_]+_\\\\d+.html\\\"\\\\s+target=\\\"_blank\\\">([^<]+)</a>\",\r\n	\"bookContentUrl\": \"https://www.bookbao8.com/views/{bookId}_{indexId}.html\",\r\n	\"contentStart\": \"<dd id=\\\"contents\\\">\",\r\n	\"contentEnd\": \"</dd>\"\r\n}', '0', '2020-05-04 17:42:22', '2020-05-04 17:42:22');
INSERT INTO `crawl_source` VALUES ('4', '书趣阁', '{\r\n	\"bookListUrl\": \"http://m.shuquge.com/sort/{catId}/0_{page}.html\",\r\n	\"catIdRule\": {\r\n		\"catId1\": \"1\",\r\n		\"catId2\": \"2\",\r\n		\"catId3\": \"3\",\r\n		\"catId4\": \"4\",\r\n		\"catId5\": \"7\",\r\n		\"catId6\": \"6\",\r\n		\"catId7\": \"8\"\r\n	},\r\n	\"bookIdPatten\": \"href=\\\"/s/(\\\\d+)\\\\.html\\\"\",\r\n	\"pagePatten\": \"第(\\\\d+)/\\\\d+页\",\r\n	\"totalPagePatten\": \"第\\\\d+/(\\\\d+)页\",\r\n	\"bookDetailUrl\": \"http://m.shuquge.com/s/{bookId}.html\",\r\n	\"bookNamePatten\": \"<a\\\\s+href=\\\"/s/\\\\d+\\\\.html\\\"><h2>([^/]+)</h2></a>\",\r\n	\"authorNamePatten\": \"<p>作者：([^/]+)</p>\",\r\n	\"picUrlPatten\": \"src=\\\"(http://www.shuquge.com/files/article/image/\\\\d+/\\\\d+/\\\\d+s\\\\.jpg)\\\"\",\r\n	\"statusPatten\": \"<p>状态：([^/]+)</p>\",\r\n	\"bookStatusRule\": {\r\n		\"连载中\": 0,\r\n		\"完本\": 1\r\n	},\r\n	\"descStart\": \"<div class=\\\"intro_info\\\">\",\r\n	\"descEnd\": \"最新章节推荐地址\",\r\n	\"bookIndexUrl\": \"http://www.shuquge.com/txt/{bookId}/index.html\",\r\n	\"bookIndexStart\": \"》正文\",\r\n	\"indexIdPatten\": \"<dd><a\\\\s+href=\\\"(\\\\d+)\\\\.html\\\">[^/]+</a></dd>\",\r\n	\"indexNamePatten\": \"<dd><a\\\\s+href=\\\"\\\\d+\\\\.html\\\">([^/]+)</a></dd>\",\r\n	\"bookContentUrl\": \"http://www.shuquge.com/txt/{bookId}/{indexId}.html\",\r\n	\"contentStart\": \"<div id=\\\"content\\\" class=\\\"showtxt\\\">\",\r\n	\"contentEnd\": \"http://www.shuquge.com\"\r\n}', '1', '2020-05-18 12:02:34', '2020-05-18 12:02:34');
INSERT INTO `crawl_source` (`id`, `source_name`, `crawl_rule`, `source_status`, `create_time`, `update_time`) VALUES ('5', '笔趣阁', '{\"bookListUrl\":\"http://m.mcmssc.com/xclass/{catId}/{page}.html\",\"catIdRule\":{\"catId1\":\"1\",\"catId2\":\"2\",\"catId3\":\"3\",\"catId4\":\"4\",\"catId5\":\"5\",\"catId6\":\"6\",\"catId7\":\"7\"},\"bookIdPatten\":\"href=\\\"/(\\\\d+_\\\\d+)/\\\"\",\"pagePatten\":\"class=\\\"page_txt\\\"\\\\s+value=\\\"(\\\\d+)/\\\\d+\\\"\\\\s+size=\",\"totalPagePatten\":\"class=\\\"page_txt\\\"\\\\s+value=\\\"\\\\d+/(\\\\d+)\\\"\\\\s+size=\",\"bookDetailUrl\":\"http://m.mcmssc.com/{bookId}/\",\"bookNamePatten\":\"<span\\\\s+class=\\\"title\\\">([^/]+)</span>\",\"authorNamePatten\":\"<a\\\\s+href=\\\"/author/\\\\d+/\\\">([^/]+)</a>\",\"picUrlPatten\":\"<img\\\\s+src=\\\"([^>]+)\\\"\\\\s+onerror=\",\"picUrlPrefix\":\"http://m.mcmssc.com/\",\"statusPatten\":\">状态：([^/]+)<\",\"bookStatusRule\":{\"连载\":0,\"全本\":1},\"visitCountPatten\":\">点击：(\\\\d+)<\",\"descStart\":\"<p class=\\\"review\\\">\",\"descEnd\":\"</p>\",\"bookIndexUrl\":\"http://m.mcmssc.com/{bookId}/all.html\",\"indexIdPatten\":\"<a\\\\s+href=\\\"/\\\\d+_\\\\d+/(\\\\d+)\\\\.html\\\">[^/]+</a>\",\"indexNamePatten\":\"<a\\\\s+href=\\\"/\\\\d+_\\\\d+/\\\\d+\\\\.html\\\">([^/]+)</a>\",\"bookContentUrl\":\"http://www.mcmssc.com/{bookId}/{indexId}.html\",\"contentStart\":\"</p>\",\"contentEnd\":\"<div align=\\\"center\\\">\"}', '1', '2020-05-18 15:57:41', '2020-05-18 15:57:41');

-- ----------------------------
-- Table structure for friend_link
-- ----------------------------
DROP TABLE IF EXISTS `friend_link`;
CREATE TABLE `friend_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `link_name` varchar(50) NOT NULL COMMENT '链接名',
  `link_url` varchar(100) NOT NULL COMMENT '链接url',
  `sort` tinyint(4) NOT NULL DEFAULT '11' COMMENT '排序号',
  `is_open` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否开启，0：不开启，1：开启',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新者用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of friend_link
-- ----------------------------
INSERT INTO `friend_link` VALUES ('5', '小说精品屋', 'https://www.xinshumen.com', '11', '1', null, null, null, null);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cat_id` int(11) DEFAULT NULL COMMENT '类别ID',
  `cat_name` varchar(50) DEFAULT NULL COMMENT '分类名',
  `source_name` varchar(50) DEFAULT NULL COMMENT '来源',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` text COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '发布人ID',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='新闻表';

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES ('1', '1', '招募', '小说精品屋', '2019小说精品屋作者福利&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;高价保底买断征稿', '2019小说精品屋作者福利&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;高价保底买断征稿', '2020-04-27 15:42:21', null, '2020-04-27 15:42:26', null);
INSERT INTO `news` VALUES ('2', '3', '公告', '小说精品屋', '编辑联系方式及征稿类型&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;版权商务联系方式', '编辑联系方式及征稿类型&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;版权商务联系方式', '2020-04-28 15:44:07', null, '2020-04-28 15:44:12', null);

-- ----------------------------
-- Table structure for news_category
-- ----------------------------
DROP TABLE IF EXISTS `news_category`;
CREATE TABLE `news_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '分类名',
  `sort` tinyint(4) NOT NULL DEFAULT '10' COMMENT '排序',
  `create_user_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_user_id` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='新闻类别表';

-- ----------------------------
-- Records of news_category
-- ----------------------------
INSERT INTO `news_category` VALUES ('1', '招募', '10', null, null, null, null);
INSERT INTO `news_category` VALUES ('3', '公告', '11', null, null, null, null);

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

-- ----------------------------
-- Records of order_pay
-- ----------------------------

-- ----------------------------
-- Table structure for sys_data_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_perm`;
CREATE TABLE `sys_data_perm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `table_name` varchar(50) DEFAULT NULL COMMENT '数据表名称',
  `module_name` varchar(50) DEFAULT NULL COMMENT '所属模块',
  `crl_attr_name` varchar(50) DEFAULT NULL COMMENT '用户权限控制属性名',
  `crl_column_name` varchar(50) DEFAULT NULL COMMENT '数据表权限控制列名',
  `perm_code` varchar(50) DEFAULT NULL COMMENT '权限code，all_开头表示查看所有数据的权限，sup_开头表示查看下级数据的权限，own_开头表示查看本级数据的权限',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8 COMMENT='数据权限管理';

-- ----------------------------
-- Records of sys_data_perm
-- ----------------------------
INSERT INTO `sys_data_perm` VALUES ('210', '查看用户表全部数据', 'sys_user', '用户管理', 'deptId', 'dept_id', 'all_dept_sys_user', '1', null, null);
INSERT INTO `sys_data_perm` VALUES ('211', '查看用户表下级部门数据', 'sys_user', '用户管理', 'deptId', 'dept_id', 'sup_dept_sys_user', '2', null, null);
INSERT INTO `sys_data_perm` VALUES ('212', '查看用户表本部门数据', 'sys_user', '用户管理', 'deptId', 'dept_id', 'own_dept_sys_user', '3', null, null);
INSERT INTO `sys_data_perm` VALUES ('213', '查看用户表个人数据', 'sys_user', '用户管理', 'userId', 'user_id', 'own_user_sys_user', '4', null, null);
INSERT INTO `sys_data_perm` VALUES ('214', '查看下级部门订单数据', 'fb_order', '订单管理', 'deptId', 'dept_id', 'sup_dept_fb_order', '2', null, null);
INSERT INTO `sys_data_perm` VALUES ('215', '查看本部门订单数据', 'fb_order', '订单管理', 'deptId', 'dept_id', 'own_dept_fb_order', '3', null, null);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('6', '0', '研发部', '1', '1');
INSERT INTO `sys_dept` VALUES ('7', '6', '研發一部', '1', '1');
INSERT INTO `sys_dept` VALUES ('8', '6', '研发二部', '2', '1');
INSERT INTO `sys_dept` VALUES ('9', '0', '销售部', '2', '1');
INSERT INTO `sys_dept` VALUES ('10', '9', '销售一部', '1', '1');
INSERT INTO `sys_dept` VALUES ('11', '0', '产品部', '3', '1');
INSERT INTO `sys_dept` VALUES ('12', '11', '产品一部', '1', '1');
INSERT INTO `sys_dept` VALUES ('13', '0', '测试部', '5', '1');
INSERT INTO `sys_dept` VALUES ('14', '13', '测试一部', '1', '1');
INSERT INTO `sys_dept` VALUES ('15', '13', '测试二部', '2', '1');
INSERT INTO `sys_dept` VALUES ('16', '13', '测试三部', '13', '1');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '标签名',
  `value` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '数据值',
  `type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序（升序）',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父级编号',
  `create_by` int(11) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`name`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('1', '正常', '0', 'del_flag', '删除标记', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('3', '显示', '1', 'show_hide', '显示/隐藏', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('4', '隐藏', '0', 'show_hide', '显示/隐藏', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('5', '是', '1', 'yes_no', '是/否', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('6', '否', '0', 'yes_no', '是/否', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('7', '红色', 'red', 'color', '颜色值', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('8', '绿色', 'green', 'color', '颜色值', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('9', '蓝色', 'blue', 'color', '颜色值', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('10', '黄色', 'yellow', 'color', '颜色值', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('11', '橙色', 'orange', 'color', '颜色值', '50', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('12', '默认主题', 'default', 'theme', '主题方案', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('13', '天蓝主题', 'cerulean', 'theme', '主题方案', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('14', '橙色主题', 'readable', 'theme', '主题方案', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('15', '红色主题', 'united', 'theme', '主题方案', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('16', 'Flat主题', 'flat', 'theme', '主题方案', '60', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('17', '国家', '1', 'sys_area_type', '区域类型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('18', '省份、直辖市', '2', 'sys_area_type', '区域类型', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('19', '地市', '3', 'sys_area_type', '区域类型', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('20', '区县', '4', 'sys_area_type', '区域类型', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('21', '公司', '1', 'sys_office_type', '机构类型', '60', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('22', '部门', '2', 'sys_office_type', '机构类型', '70', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('23', '小组', '3', 'sys_office_type', '机构类型', '80', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('24', '其它', '4', 'sys_office_type', '机构类型', '90', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('25', '综合部', '1', 'sys_office_common', '快捷通用部门', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('26', '开发部', '2', 'sys_office_common', '快捷通用部门', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('27', '人力部', '3', 'sys_office_common', '快捷通用部门', '50', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('28', '一级', '1', 'sys_office_grade', '机构等级', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('29', '二级', '2', 'sys_office_grade', '机构等级', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('30', '三级', '3', 'sys_office_grade', '机构等级', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('31', '四级', '4', 'sys_office_grade', '机构等级', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('32', '所有数据', '1', 'sys_data_scope', '数据范围', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('33', '所在公司及以下数据', '2', 'sys_data_scope', '数据范围', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('34', '所在公司数据', '3', 'sys_data_scope', '数据范围', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('35', '所在部门及以下数据', '4', 'sys_data_scope', '数据范围', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('36', '所在部门数据', '5', 'sys_data_scope', '数据范围', '50', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('37', '仅本人数据', '8', 'sys_data_scope', '数据范围', '90', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('38', '按明细设置', '9', 'sys_data_scope', '数据范围', '100', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('39', '系统管理', '1', 'sys_user_type', '用户类型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('40', '部门经理', '2', 'sys_user_type', '用户类型', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('41', '普通用户', '3', 'sys_user_type', '用户类型', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('42', '基础主题', 'basic', 'cms_theme', '站点主题', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('43', '蓝色主题', 'blue', 'cms_theme', '站点主题', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('44', '红色主题', 'red', 'cms_theme', '站点主题', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('45', '文章模型', 'article', 'cms_module', '栏目模型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('46', '图片模型', 'picture', 'cms_module', '栏目模型', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('47', '下载模型', 'download', 'cms_module', '栏目模型', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('48', '链接模型', 'link', 'cms_module', '栏目模型', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('49', '专题模型', 'special', 'cms_module', '栏目模型', '50', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('50', '默认展现方式', '0', 'cms_show_modes', '展现方式', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('51', '首栏目内容列表', '1', 'cms_show_modes', '展现方式', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('52', '栏目第一条内容', '2', 'cms_show_modes', '展现方式', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('53', '发布', '0', 'cms_del_flag', '内容状态', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('54', '删除', '1', 'cms_del_flag', '内容状态', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('55', '审核', '2', 'cms_del_flag', '内容状态', '15', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('56', '首页焦点图', '1', 'cms_posid', '推荐位', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('57', '栏目页文章推荐', '2', 'cms_posid', '推荐位', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('58', '咨询', '1', 'cms_guestbook', '留言板分类', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('59', '建议', '2', 'cms_guestbook', '留言板分类', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('60', '投诉', '3', 'cms_guestbook', '留言板分类', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('61', '其它', '4', 'cms_guestbook', '留言板分类', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('62', '公休', '1', 'oa_leave_type', '请假类型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('63', '病假', '2', 'oa_leave_type', '请假类型', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('64', '事假', '3', 'oa_leave_type', '请假类型', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('65', '调休', '4', 'oa_leave_type', '请假类型', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('66', '婚假', '5', 'oa_leave_type', '请假类型', '60', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('67', '接入日志', '1', 'sys_log_type', '日志类型', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('68', '异常日志', '2', 'sys_log_type', '日志类型', '40', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('69', '请假流程', 'leave', 'act_type', '流程类型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('70', '审批测试流程', 'test_audit', 'act_type', '流程类型', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('71', '分类1', '1', 'act_category', '流程分类', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('72', '分类2', '2', 'act_category', '流程分类', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('73', '增删改查', 'crud', 'gen_category', '代码生成分类', '10', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('74', '增删改查（包含从表）', 'crud_many', 'gen_category', '代码生成分类', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('75', '树结构', 'tree', 'gen_category', '代码生成分类', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('76', '=', '=', 'gen_query_type', '查询方式', '10', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('77', '!=', '!=', 'gen_query_type', '查询方式', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('78', '&gt;', '&gt;', 'gen_query_type', '查询方式', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('79', '&lt;', '&lt;', 'gen_query_type', '查询方式', '40', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('80', 'Between', 'between', 'gen_query_type', '查询方式', '50', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('81', 'Like', 'like', 'gen_query_type', '查询方式', '60', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('82', 'Left Like', 'left_like', 'gen_query_type', '查询方式', '70', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('83', 'Right Like', 'right_like', 'gen_query_type', '查询方式', '80', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('84', '文本框', 'input', 'gen_show_type', '字段生成方案', '10', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('85', '文本域', 'textarea', 'gen_show_type', '字段生成方案', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('86', '下拉框', 'select', 'gen_show_type', '字段生成方案', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('87', '复选框', 'checkbox', 'gen_show_type', '字段生成方案', '40', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('88', '单选框', 'radiobox', 'gen_show_type', '字段生成方案', '50', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('89', '日期选择', 'dateselect', 'gen_show_type', '字段生成方案', '60', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('90', '人员选择', 'userselect', 'gen_show_type', '字段生成方案', '70', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('91', '部门选择', 'officeselect', 'gen_show_type', '字段生成方案', '80', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('92', '区域选择', 'areaselect', 'gen_show_type', '字段生成方案', '90', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('93', 'String', 'String', 'gen_java_type', 'Java类型', '10', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('94', 'Long', 'Long', 'gen_java_type', 'Java类型', '20', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('95', '仅持久层', 'dao', 'gen_category', '代码生成分类', '40', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('96', '男', '1', 'sex', '性别', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('97', '女', '2', 'sex', '性别', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('98', 'Integer', 'Integer', 'gen_java_type', 'Java类型', '30', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('99', 'Double', 'Double', 'gen_java_type', 'Java类型', '40', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('100', 'Date', 'java.util.Date', 'gen_java_type', 'Java类型', '50', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('104', 'Custom', 'Custom', 'gen_java_type', 'Java类型', '90', '0', '1', null, '1', null, null, '1');
INSERT INTO `sys_dict` VALUES ('105', '会议通告', '1', 'oa_notify_type', '通知通告类型', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('106', '奖惩通告', '2', 'oa_notify_type', '通知通告类型', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('107', '活动通告', '3', 'oa_notify_type', '通知通告类型', '30', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('108', '草稿', '0', 'oa_notify_status', '通知通告状态', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('109', '发布', '1', 'oa_notify_status', '通知通告状态', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('110', '未读', '0', 'oa_notify_read', '通知通告状态', '10', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('111', '已读', '1', 'oa_notify_read', '通知通告状态', '20', '0', '1', null, '1', null, null, '0');
INSERT INTO `sys_dict` VALUES ('112', '草稿', '0', 'oa_notify_status', '通知通告状态', '10', '0', '1', null, '1', null, '', '0');
INSERT INTO `sys_dict` VALUES ('113', '删除', '1', 'del_flag', '删除标记', null, null, null, null, null, null, '', '');
INSERT INTO `sys_dict` VALUES ('121', '编码', 'code', 'hobby', '爱好', null, null, null, null, null, null, '', '');
INSERT INTO `sys_dict` VALUES ('122', '绘画', 'painting', 'hobby', '爱好', null, null, null, null, null, null, '', '');
INSERT INTO `sys_dict` VALUES ('123', 'Integer', 'Integer', 'java_type', 'Java数据类型', '1', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('124', 'Long', 'Long', 'java_type', 'Java数据类型', '2', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('125', 'Float', 'Float', 'java_type', 'Java数据类型', '3', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('126', 'Double', 'Double', 'java_type', 'Java数据类型', '4', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('127', 'BigDecimal', 'BigDecimal', 'java_type', 'Java数据类型', '5', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('128', 'Boolean', 'Boolean', 'java_type', 'Java数据类型', '6', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('129', 'String', 'String', 'java_type', 'Java数据类型', '7', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('130', 'Date', 'Date', 'java_type', 'Java数据类型', '8', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('131', '文本框', '1', 'page_type', '页面显示类型', '1', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('132', '下拉框', '2', 'page_type', '页面显示类型', '2', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('133', '数值', '3', 'page_type', '页面显示类型', '3', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('134', '日期', '4', 'page_type', '页面显示类型', '4', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('135', '文本域', '5', 'page_type', '页面显示类型', '5', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('136', '富文本', '6', 'page_type', '页面显示类型', '6', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('137', '上传图片【单文件】', '7', 'page_type', '页面显示类型', '7', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('138', '隐藏域', '11', 'page_type', '页面显示类型', '11', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('139', '不显示', '12', 'page_type', '页面显示类型', '12', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('140', '男频', '0', 'work_direction', '作品方向', '0', null, null, null, null, null, '', null);
INSERT INTO `sys_dict` VALUES ('141', '女频', '1', 'work_direction', '作品方向', '1', null, null, null, null, null, '', null);

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '文件类型',
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8 COMMENT='文件上传';

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES ('142', '0', '/files/658554ff-cd62-4ca2-936d-62e35f8af5ef.png', '2019-11-01 16:13:39');
INSERT INTO `sys_file` VALUES ('143', '0', '/files/de40bb48-c278-4360-9ee6-80b464419255.png', '2019-11-01 16:14:24');
INSERT INTO `sys_file` VALUES ('144', '0', '/files/8b0737fb-e68d-4162-a066-05f1f3f66b0f.png', '2019-11-02 19:42:03');
INSERT INTO `sys_file` VALUES ('145', '0', '/files/1006a87c-ee4e-4e97-8bcd-2b5cf861b826.png', '2019-11-02 19:42:28');
INSERT INTO `sys_file` VALUES ('146', '0', '/files/d7834c20-0e29-4c92-8d0a-9b1297b6e5b8.png', '2019-11-02 19:43:05');
INSERT INTO `sys_file` VALUES ('147', '0', '/files/6e5d38de-4366-459a-a498-7e418e746f62.png', '2019-11-02 19:45:59');
INSERT INTO `sys_file` VALUES ('148', '0', '/files/e34d60a9-6bde-48c0-ac4c-64a5ddffcdd4.jpg', '2019-11-02 19:54:49');
INSERT INTO `sys_file` VALUES ('149', '0', '/files/545579fa-ab81-42e2-8bfa-13ebdc7a137d.png', '2019-11-09 10:39:05');
INSERT INTO `sys_file` VALUES ('150', '1', '/files/fe0fe8db-495f-4c23-8c74-744399f5c1af.txt', '2019-11-09 17:28:31');
INSERT INTO `sys_file` VALUES ('151', '99', '/files/1a710af4-a766-4ed8-b8e9-2ec5ef25df6b.sql', '2019-11-09 17:42:59');
INSERT INTO `sys_file` VALUES ('152', '99', '/files/3a984623-4d2c-4122-9b47-6054eb670dc9.sql', '2019-11-09 17:43:04');
INSERT INTO `sys_file` VALUES ('153', '0', '/files/2019/11/09/44eddafe-1c58-4710-a2ba-3f88d0e77958.png', '2019-11-09 18:29:26');
INSERT INTO `sys_file` VALUES ('154', '0', '/files/2019/11/23/f0dfac47-7fc4-43e3-aab9-f5f2276ac550.png', '2019-11-23 11:29:03');
INSERT INTO `sys_file` VALUES ('155', '0', '/files/2019/11/23/c7ed1b36-63dc-4259-bb31-e2f8d86234de.png', '2019-11-23 11:29:06');
INSERT INTO `sys_file` VALUES ('156', '0', '/files/2019/11/23/4c772801-5cce-40ce-bd3c-603bb30d5ff4.png', '2019-11-23 13:18:11');
INSERT INTO `sys_file` VALUES ('157', '0', '/files/2019/11/23/3878019f-6799-4ac4-9a6b-4c709cca5610.png', '2019-11-23 13:18:50');
INSERT INTO `sys_file` VALUES ('158', '0', '/files/2019/11/23/94bac082-7358-4d84-a3e2-ae8ce31cc427.png', '2019-11-23 13:30:38');
INSERT INTO `sys_file` VALUES ('159', '0', '/files/2019/11/23/d5ae3fa2-deae-4703-93b8-723d372895a6.png', '2019-11-23 13:31:09');
INSERT INTO `sys_file` VALUES ('160', '0', '/files/2019/11/23/7b7beb05-9c02-40a1-b25f-db2366d76c87.png', '2019-11-23 13:33:04');
INSERT INTO `sys_file` VALUES ('161', '0', '/files/2019/11/23/b9deb454-b243-4685-8c69-ae2f302a079f.png', '2019-11-23 14:29:52');
INSERT INTO `sys_file` VALUES ('162', '0', '/files/2019/11/23/b33b6601-7578-4c58-86bf-d4099d53e752.png', '2019-11-23 14:30:32');
INSERT INTO `sys_file` VALUES ('163', '0', '/files/2019/11/23/16ed3b6a-c5c8-442e-a166-3f43f45cfc0f.png', '2019-11-23 14:33:53');
INSERT INTO `sys_file` VALUES ('164', '0', '/files/2019/11/23/9b768a28-d720-4f00-a019-9f3136f61ac4.png', '2019-11-23 14:34:22');
INSERT INTO `sys_file` VALUES ('165', '0', '/files/2019/11/23/51efde05-3096-4eda-a07e-fc44a939e909.png', '2019-11-23 15:15:13');
INSERT INTO `sys_file` VALUES ('166', '0', '/files/2019/11/23/eb590e78-f1e3-4d6a-879c-02f9e5028519.png', '2019-11-23 15:22:34');
INSERT INTO `sys_file` VALUES ('167', '0', '/files/2019/11/23/2d974db3-1292-47e8-8d37-5696d63fd737.png', '2019-11-23 15:23:11');
INSERT INTO `sys_file` VALUES ('168', '0', '/files/2019/11/23/afbf87aa-1ee9-4244-8f3c-a06dc75b950a.png', '2019-11-23 15:24:06');
INSERT INTO `sys_file` VALUES ('169', '0', '/files/2019/11/23/e1d27b6b-9c45-4309-9861-9bdb77dbea4c.png', '2019-11-23 15:34:14');
INSERT INTO `sys_file` VALUES ('170', '0', '/files/2019/11/23/25f40d3b-965d-4564-b153-052139446149.png', '2019-11-23 15:34:59');
INSERT INTO `sys_file` VALUES ('171', '0', '/files/2019/11/23/01c6e8ea-e25c-4191-b12c-0884391157c5.png', '2019-11-23 15:50:06');
INSERT INTO `sys_file` VALUES ('172', '0', '/files/2019/11/23/3a230ea3-93dc-4bf7-9037-94a013b3408c.png', '2019-11-23 15:51:04');
INSERT INTO `sys_file` VALUES ('173', '0', '/files/2019/11/23/4b6994ad-7629-48dc-9c15-c2cf01900b96.png', '2019-11-23 15:58:25');
INSERT INTO `sys_file` VALUES ('174', '0', '/files/2019/11/23/298fb24b-3e1f-4e97-88d7-2df41acffb8d.png', '2019-11-23 15:59:26');
INSERT INTO `sys_file` VALUES ('175', '0', '/files/2019/11/23/8e314673-ed11-4b58-942c-68ac34ade62d.png', '2019-11-23 16:10:07');
INSERT INTO `sys_file` VALUES ('176', '0', '/files/2019/11/23/4497dc84-518e-4b4f-b5f1-a38922827c00.png', '2019-11-23 16:11:19');
INSERT INTO `sys_file` VALUES ('177', '0', '/files/2019/11/23/81215d73-5922-4d99-8134-710d8a1505f5.png', '2019-11-23 16:59:39');
INSERT INTO `sys_file` VALUES ('178', '0', '/files/2019/11/23/22be71d0-e2e4-4c80-9db3-0955acf3a3b4.png', '2019-11-23 17:00:00');
INSERT INTO `sys_file` VALUES ('179', '0', '/files/2019/11/23/c92a51b2-0ee1-4f9a-aa9f-18da61d07e8f.png', '2019-11-23 17:13:28');
INSERT INTO `sys_file` VALUES ('180', '0', '/files/2019/11/23/eb3f9286-a8b2-46a1-9d1d-54d1b2170b0d.png', '2019-11-23 17:14:16');
INSERT INTO `sys_file` VALUES ('181', '0', '/files/2019/11/23/912af35f-8514-408d-9b21-f226e52f8611.png', '2019-11-23 17:33:16');
INSERT INTO `sys_file` VALUES ('182', '0', '/files/2019/11/23/ff9e2850-6d60-4a6b-a93a-d0e58b5dc21c.png', '2019-11-23 17:34:09');

-- ----------------------------
-- Table structure for sys_gen_columns
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_columns`;
CREATE TABLE `sys_gen_columns` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '表名',
  `column_name` varchar(64) CHARACTER SET utf8 DEFAULT '' COMMENT '列名',
  `column_type` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(64) CHARACTER SET utf8 DEFAULT NULL COMMENT '映射java类型',
  `column_comment` varchar(1024) CHARACTER SET utf8 DEFAULT '' COMMENT '列注释',
  `column_sort` tinyint(4) DEFAULT NULL COMMENT '列排序（升序）',
  `column_label` varchar(64) DEFAULT NULL COMMENT '鍒楁爣绛惧悕',
  `page_type` tinyint(4) DEFAULT '1' COMMENT '页面显示类型：1、文本框 2、下拉框 3、数值4、日期 5、文本域6、富文本 7、上传图片【单文件】 8、上传图片【多文件】9、上传文件【单文件】 10、上传文件【多文件】11、隐藏域 12、不显示',
  `is_required` tinyint(1) DEFAULT NULL COMMENT '是否必填',
  `dict_type` varchar(100) CHARACTER SET utf8 DEFAULT '' COMMENT '页面显示为下拉时使用，字典类型从字典表中取出',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=815 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_gen_columns
-- ----------------------------
INSERT INTO `sys_gen_columns` VALUES ('452', 'sys_user', 'username', 'varchar', 'String', '用户名', '2', '用户名', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('453', 'sys_user', 'name', 'varchar', 'String', '', '3', '真实姓名', '6', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('454', 'sys_user', 'password', 'varchar', 'String', '密码', '4', '密码', '3', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('455', 'sys_user', 'dept_id', 'bigint', 'Long', '', '5', '部门', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('456', 'sys_user', 'email', 'varchar', 'String', '邮箱', '6', '邮箱', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('457', 'sys_user', 'mobile', 'varchar', 'String', '手机号', '7', '手机号', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('458', 'sys_user', 'status', 'tinyint', 'Integer', '状态 0:禁用，1:正常', '8', '状态 0:禁用，1:正常', '2', '0', 'yes_no');
INSERT INTO `sys_gen_columns` VALUES ('459', 'sys_user', 'user_id_create', 'bigint', 'Long', '创建用户id', '9', '创建用户id', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('460', 'sys_user', 'gmt_create', 'datetime', 'Date', '创建时间', '10', '创建时间', '4', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('461', 'sys_user', 'gmt_modified', 'datetime', 'Date', '修改时间', '11', '修改时间', '4', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('462', 'sys_user', 'sex', 'bigint', 'Long', '性别', '12', '性别', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('463', 'sys_user', 'birth', 'datetime', 'Date', '出身日期', '13', '出身日期', '4', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('464', 'sys_user', 'pic_id', 'bigint', 'Long', '', '14', '', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('465', 'sys_user', 'live_address', 'varchar', 'String', '现居住地', '50', '现居住地', '6', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('466', 'sys_user', 'hobby', 'varchar', 'String', '爱好', '16', '爱好', '7', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('467', 'sys_user', 'province', 'varchar', 'String', '省份', '17', '省份', '2', '0', 'theme');
INSERT INTO `sys_gen_columns` VALUES ('468', 'sys_user', 'city', 'varchar', 'String', '所在城市', '18', '所在城市', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('469', 'sys_user', 'district', 'varchar', 'String', '所在地区', '19', '所在地区', '7', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('730', 'sys_role_data_perm', 'role_id', 'bigint', 'Long', '角色ID', '2', '角色ID', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('731', 'sys_role_data_perm', 'perm_id', 'bigint', 'Long', '权限ID', '3', '权限ID', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('732', 'sys_data_perm', 'name', 'varchar', 'String', '权限名称', '2', '权限名称', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('733', 'sys_data_perm', 'table_name', 'varchar', 'String', '数据表名称', '3', '数据表名称', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('734', 'sys_data_perm', 'module_name', 'varchar', 'String', '所属模块', '4', '所属模块', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('735', 'sys_data_perm', 'crl_attr_name', 'varchar', 'String', '用户权限控制属性名', '5', '用户权限控制属性名', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('736', 'sys_data_perm', 'crl_column_name', 'varchar', 'String', '数据表权限控制列名', '6', '数据表权限控制列名', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('737', 'sys_data_perm', 'perm_code', 'varchar', 'String', '权限code，all_开头表示查看所有数据的权限，sup_开头表示查看下级数据的权限，own_开头表示查看本级数据的权限', '7', '权限code', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('738', 'sys_data_perm', 'order_num', 'int', 'Integer', '排序', '8', '排序', '3', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('739', 'sys_data_perm', 'gmt_create', 'datetime', 'Date', '创建时间', '9', '创建时间', '12', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('740', 'sys_data_perm', 'gmt_modified', 'datetime', 'Date', '修改时间', '10', '修改时间', '12', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('771', 'fb_order', 'fb_merchant_code', 'varchar', 'String', '付呗商户号', '4', '付呗商户号', '1', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('772', 'fb_order', 'merchant_order_sn', 'varchar', 'String', '第三方商户的订单号', '5', '第三方商户的订单号', '1', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('773', 'fb_order', 'order_sn', 'varchar', 'String', '付呗订单号', '6', '付呗订单号', '2', '0', 'color');
INSERT INTO `sys_gen_columns` VALUES ('774', 'fb_order', 'platform_order_no', 'varchar', 'String', '平台方订单号', '7', '平台方订单号', '2', '0', 'oa_leave_type');
INSERT INTO `sys_gen_columns` VALUES ('775', 'fb_order', 'trade_no', 'varchar', 'String', '商户单号', '8', '商户单号', '6', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('776', 'fb_order', 'order_state', 'tinyint', 'Integer', '订单状态，1：未支付，2：支付成功，3：支付失败，4：支付取消', '9', '订单状态，1：未支付，2：支付成功，3：支付失败，4：支付取消', '2', '0', 'yes_no');
INSERT INTO `sys_gen_columns` VALUES ('777', 'fb_order', 'fn_coupon', 'decimal', 'Double', '蜂鸟优惠卷抵扣', '10', '蜂鸟优惠卷抵扣', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('778', 'fb_order', 'red_packet', 'decimal', 'BigDecimal', '红包抵扣', '11', '红包抵扣', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('779', 'fb_order', 'total_fee', 'decimal', 'BigDecimal', '实收金额(元)', '12', '实收金额(元)', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('780', 'fb_order', 'order_price', 'decimal', 'BigDecimal', '订单金额', '13', '订单金额', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('781', 'fb_order', 'fee', 'decimal', 'BigDecimal', '手续费(元)', '14', '手续费(元)', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('782', 'fb_order', 'body', 'varchar', 'String', '对商品或交易的描述', '15', '对商品或交易的描述', '7', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('783', 'fb_order', 'attach', 'varchar', 'String', '附加数据', '16', '附加数据', '6', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('784', 'fb_order', 'store_id', 'bigint', 'Long', '付呗系统的门店id', '17', '付呗系统的门店id', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('785', 'fb_order', 'cashier_id', 'bigint', 'Long', '付呗系统的收银员id', '18', '付呗系统的收银员id', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('786', 'fb_order', 'device_no', 'varchar', 'String', '设备终端号', '19', '设备终端号', '1', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('787', 'fb_order', 'user_id', 'varchar', 'String', '微信顾客支付授权的“open_id”或者支付宝顾客的“buyer_user_id”', '20', '微信顾客支付授权的“open_id”或者支付宝顾客的“buyer_user_id”', '1', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('788', 'fb_order', 'user_logon_id', 'varchar', 'String', '支付宝顾客的账号', '21', '支付宝顾客的账号', '5', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('789', 'fb_order', 'pay_time', 'datetime', 'Date', '交易成功的时间', '22', '交易成功的时间', '4', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('790', 'fb_order', 'pay_channel', 'tinyint', 'Integer', '支付通道:1微信、2支付宝、3银联', '23', '支付通道:1微信、2支付宝、3银联', '2', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('791', 'fb_order', 'no_cash_coupon_fee', 'decimal', 'BigDecimal', '免充值代金券金额(元)', '24', '免充值代金券金额(元)', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('792', 'fb_order', 'cash_coupon_fee', 'decimal', 'BigDecimal', '预充值代金券金额(元)', '25', '预充值代金券金额(元)', '3', '0', 'yes_no');
INSERT INTO `sys_gen_columns` VALUES ('793', 'fb_order', 'cash_fee', 'decimal', 'BigDecimal', '顾客实际支付金额(元)', '26', '顾客实际支付金额(元)', '3', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('794', 'fb_order', 'sign', 'varchar', 'String', '签名', '27', '签名', '2', '0', 'theme');
INSERT INTO `sys_gen_columns` VALUES ('795', 'fb_order', 'options', 'varchar', 'String', '其它选项', '28', '其它选项', '7', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('796', 'fb_order', 'create_time', 'datetime', 'Date', '创建时间', '29', '创建时间', '4', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('797', 'fb_order', 'push_time', 'datetime', 'Date', '推送时间', '30', '推送时间', '4', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('798', 'fb_order', 'push_ip', 'varchar', 'String', '推送IP', '31', '推送IP', '6', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('799', 'fb_order', 'mcht_id', 'bigint', 'BigDecimal', '商户id', '90', '商户id', '3', '0', 'theme');
INSERT INTO `sys_gen_columns` VALUES ('800', 'fb_order', 'sn', 'char', 'String', 'QR编号', '100', 'QR编号', '1', '0', 'del_flag');
INSERT INTO `sys_gen_columns` VALUES ('801', 'author', 'user_id', 'bigint', 'Long', '用户ID', '2', '用户ID', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('802', 'author', 'invite_code', 'varchar', 'String', '邀请码', '3', '邀请码', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('803', 'author', 'pen_name', 'varchar', 'String', '笔名', '4', '笔名', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('804', 'author', 'tel_phone', 'varchar', 'String', '手机号码', '5', '手机号码', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('805', 'author', 'chat_account', 'varchar', 'String', 'QQ或微信账号', '6', 'QQ或微信账号', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('806', 'author', 'email', 'varchar', 'String', '电子邮箱', '7', '电子邮箱', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('807', 'author', 'work_direction', 'tinyint', 'Integer', '作品方向，0：男频，1：女频', '8', '作品方向，0：男频，1：女频', '2', '0', 'work_direction');
INSERT INTO `sys_gen_columns` VALUES ('808', 'author', 'status', 'tinyint', 'Integer', '0：正常，1：封禁', '10', '0：正常，1：封禁', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('809', 'author', 'create_time', 'datetime', 'Date', '创建时间', '9', '入驻时间', '4', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('810', 'author_code', 'invite_code', 'varchar', 'String', '邀请码', '2', '邀请码', '1', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('811', 'author_code', 'validity_time', 'datetime', 'Date', '有效时间', '3', '有效时间', '4', '1', null);
INSERT INTO `sys_gen_columns` VALUES ('812', 'author_code', 'is_use', 'tinyint', 'Integer', '是否使用过，0：未使用，1:使用过', '4', '是否使用过，0：未使用，1:使用过', '1', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('813', 'author_code', 'create_time', 'datetime', 'Date', '创建时间', '5', '创建时间', '4', '0', null);
INSERT INTO `sys_gen_columns` VALUES ('814', 'author_code', 'create_user_id', 'bigint', 'Long', '创建人ID', '6', '创建人ID', '1', '0', null);

-- ----------------------------
-- Table structure for sys_gen_table
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_table`;
CREATE TABLE `sys_gen_table` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `table_name` varchar(64) NOT NULL COMMENT '表名',
  `class_name` varchar(100) NOT NULL COMMENT '实体类名称',
  `comments` varchar(500) NOT NULL COMMENT '表说明',
  `category` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分类：0：数据表，1：树表',
  `package_name` varchar(500) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `sub_module_name` varchar(30) DEFAULT NULL COMMENT '生成子模块名',
  `function_name` varchar(200) DEFAULT NULL COMMENT '生成功能名，用于类描述',
  `function_name_simple` varchar(50) DEFAULT NULL COMMENT '生成功能名（简写），用于功能提示，如“保存xx成功”',
  `author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `src_dir` varchar(1000) DEFAULT NULL COMMENT 'src目录',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` bigint(20) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint(20) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码生成表';

-- ----------------------------
-- Records of sys_gen_table
-- ----------------------------
INSERT INTO `sys_gen_table` VALUES ('1', '表名', '1', '1', '0', null, null, null, null, null, null, null, null, '1', '2019-10-24 18:21:24', '1', '2019-10-24 18:21:35', null);

-- ----------------------------
-- Table structure for sys_gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_table_column`;
CREATE TABLE `sys_gen_table_column` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `table_id` bigint(20) NOT NULL COMMENT '表id',
  `column_name` varchar(64) NOT NULL COMMENT '列名',
  `column_sort` decimal(10,0) DEFAULT NULL COMMENT '列排序（升序）',
  `column_type` varchar(100) NOT NULL COMMENT '类型',
  `column_label` varchar(50) DEFAULT NULL COMMENT '列标签名',
  `comments` varchar(500) NOT NULL COMMENT '列备注说明',
  `attr_name` varchar(200) NOT NULL COMMENT '类的属性名',
  `attr_type` varchar(200) NOT NULL COMMENT '类的属性类型',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键',
  `is_null` char(1) DEFAULT NULL COMMENT '是否可为空',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否插入字段',
  `is_update` char(1) DEFAULT NULL COMMENT '是否更新字段',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段',
  `query_type` varchar(200) DEFAULT NULL COMMENT '查询方式',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段',
  `show_type` varchar(200) DEFAULT NULL COMMENT '表单类型',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  PRIMARY KEY (`id`),
  KEY `idx_gen_table_column_tn` (`table_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码生成表列';

-- ----------------------------
-- Records of sys_gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1412 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES ('1369', '-1', '获取用户信息为空', '登录', '462', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:09:21');
INSERT INTO `sys_log` VALUES ('1370', '-1', '获取用户信息为空', '登录', '19', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:09:26');
INSERT INTO `sys_log` VALUES ('1371', '1', 'admin', '登录', '98', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:09:33');
INSERT INTO `sys_log` VALUES ('1372', '1', 'admin', '请求访问主页', '372', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:09:33');
INSERT INTO `sys_log` VALUES ('1373', '1', 'admin', '请求访问主页', '28', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:12:41');
INSERT INTO `sys_log` VALUES ('1374', '1', 'admin', '编辑角色', '11', 'com.java2nb.system.controller.RoleController.edit()', null, '127.0.0.1', '2020-05-13 11:18:42');
INSERT INTO `sys_log` VALUES ('1375', '1', 'admin', '添加菜单', '2', 'com.java2nb.system.controller.MenuController.add()', null, '127.0.0.1', '2020-05-13 11:19:55');
INSERT INTO `sys_log` VALUES ('1376', '1', 'admin', '保存菜单', '225', 'com.java2nb.system.controller.MenuController.save()', null, '127.0.0.1', '2020-05-13 11:24:42');
INSERT INTO `sys_log` VALUES ('1377', '1', 'admin', '编辑菜单', '15', 'com.java2nb.system.controller.MenuController.edit()', null, '127.0.0.1', '2020-05-13 11:24:54');
INSERT INTO `sys_log` VALUES ('1378', '1', 'admin', '编辑菜单', '11', 'com.java2nb.system.controller.MenuController.edit()', null, '127.0.0.1', '2020-05-13 11:24:58');
INSERT INTO `sys_log` VALUES ('1379', '1', 'admin', '更新菜单', '241', 'com.java2nb.system.controller.MenuController.update()', null, '127.0.0.1', '2020-05-13 11:25:12');
INSERT INTO `sys_log` VALUES ('1380', '1', 'admin', '编辑菜单', '8', 'com.java2nb.system.controller.MenuController.edit()', null, '127.0.0.1', '2020-05-13 11:25:16');
INSERT INTO `sys_log` VALUES ('1381', '1', 'admin', '更新菜单', '199', 'com.java2nb.system.controller.MenuController.update()', null, '127.0.0.1', '2020-05-13 11:25:26');
INSERT INTO `sys_log` VALUES ('1382', '1', 'admin', '编辑角色', '13', 'com.java2nb.system.controller.RoleController.edit()', null, '127.0.0.1', '2020-05-13 11:26:11');
INSERT INTO `sys_log` VALUES ('1383', '1', 'admin', '更新角色', '931', 'com.java2nb.system.controller.RoleController.update()', null, '127.0.0.1', '2020-05-13 11:26:36');
INSERT INTO `sys_log` VALUES ('1384', '-1', '获取用户信息为空', '登录', '11', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:27:02');
INSERT INTO `sys_log` VALUES ('1385', '1', 'admin', '登录', '19', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:27:08');
INSERT INTO `sys_log` VALUES ('1386', '1', 'admin', '请求访问主页', '27', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:27:08');
INSERT INTO `sys_log` VALUES ('1387', '1', 'admin', '登录', '272', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:27:56');
INSERT INTO `sys_log` VALUES ('1388', '1', 'admin', '请求访问主页', '109', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:27:56');
INSERT INTO `sys_log` VALUES ('1389', '1', 'admin', '编辑角色', '8', 'com.java2nb.system.controller.RoleController.edit()', null, '127.0.0.1', '2020-05-13 11:30:36');
INSERT INTO `sys_log` VALUES ('1390', '1', 'admin', '更新角色', '567', 'com.java2nb.system.controller.RoleController.update()', null, '127.0.0.1', '2020-05-13 11:30:42');
INSERT INTO `sys_log` VALUES ('1391', '-1', '获取用户信息为空', '登录', '246', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:31:38');
INSERT INTO `sys_log` VALUES ('1392', '1', 'admin', '登录', '38', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:31:42');
INSERT INTO `sys_log` VALUES ('1393', '1', 'admin', '请求访问主页', '110', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:31:43');
INSERT INTO `sys_log` VALUES ('1394', '1', 'admin', 'error', null, 'http://127.0.0.1/test/order/list', 'org.springframework.jdbc.BadSqlGrammarException: \r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Table \'novel_plus.fb_order\' doesn\'t exist\r\n### The error may exist in file [E:\\baseprojectparent\\novel-plus\\novel-admin\\target\\classes\\mybatis\\test\\OrderMapper.xml]\r\n### The error may involve defaultParameterMap\r\n### The error occurred while setting parameters\r\n### SQL: select `id`,`fb_merchant_code`,`merchant_order_sn`,`order_sn`,`platform_order_no`,`trade_no`,`order_state`,`fn_coupon`,`red_packet`,`total_fee`,`order_price`,`fee`,`body`,`attach`,`store_id`,`cashier_id`,`device_no`,`user_id`,`user_logon_id`,`pay_time`,`pay_channel`,`no_cash_coupon_fee`,`cash_coupon_fee`,`cash_fee`,`sign`,`options`,`create_time`,`push_time`,`push_ip`,`mcht_id`,`sn` from fb_order                                      order by id desc             limit ?, ?\r\n### Cause: java.sql.SQLSyntaxErrorException: Table \'novel_plus.fb_order\' doesn\'t exist\n; bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException: Table \'novel_plus.fb_order\' doesn\'t exist', null, '2020-05-13 11:33:27');
INSERT INTO `sys_log` VALUES ('1395', '1', 'admin', '登录', '276', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:39:20');
INSERT INTO `sys_log` VALUES ('1396', '1', 'admin', '请求访问主页', '95', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:39:20');
INSERT INTO `sys_log` VALUES ('1397', '1', 'admin', '登录', '285', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:47:00');
INSERT INTO `sys_log` VALUES ('1398', '1', 'admin', '请求访问主页', '90', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:47:00');
INSERT INTO `sys_log` VALUES ('1399', '1', 'admin', '登录', '251', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 11:48:28');
INSERT INTO `sys_log` VALUES ('1400', '1', 'admin', '请求访问主页', '95', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 11:48:28');
INSERT INTO `sys_log` VALUES ('1401', '1', 'admin', '登录', '302', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 14:09:33');
INSERT INTO `sys_log` VALUES ('1402', '1', 'admin', '请求访问主页', '88', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 14:09:34');
INSERT INTO `sys_log` VALUES ('1403', '1', 'admin', '请求更改用户密码', '3', 'com.java2nb.system.controller.UserController.resetPwd()', null, '127.0.0.1', '2020-05-13 14:11:49');
INSERT INTO `sys_log` VALUES ('1404', '1', 'admin', 'admin提交更改用户密码', '140', 'com.java2nb.system.controller.UserController.adminResetPwd()', null, '127.0.0.1', '2020-05-13 14:11:50');
INSERT INTO `sys_log` VALUES ('1405', '1', 'admin', '请求更改用户密码', '4', 'com.java2nb.system.controller.UserController.resetPwd()', null, '127.0.0.1', '2020-05-13 14:12:11');
INSERT INTO `sys_log` VALUES ('1406', '1', 'admin', '登录', '275', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 14:14:26');
INSERT INTO `sys_log` VALUES ('1407', '1', 'admin', '请求访问主页', '73', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 14:14:27');
INSERT INTO `sys_log` VALUES ('1408', '1', 'admin', 'error', null, 'http://127.0.0.1/novel/author/update', 'org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 1 errors\nField error in object \'authorDO\' on field \'id\': rejected value [1,1]; codes [typeMismatch.authorDO.id,typeMismatch.id,typeMismatch.java.lang.Long,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [authorDO.id,id]; arguments []; default message [id]]; default message [Failed to convert property value of type \'java.lang.String\' to required type \'java.lang.Long\' for property \'id\'; nested exception is java.lang.NumberFormatException: For input string: \"1,1\"]', null, '2020-05-13 14:14:38');
INSERT INTO `sys_log` VALUES ('1409', '1', 'admin', 'error', null, 'http://127.0.0.1/novel/author/update', 'org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 1 errors\nField error in object \'authorDO\' on field \'id\': rejected value [1,1]; codes [typeMismatch.authorDO.id,typeMismatch.id,typeMismatch.java.lang.Long,typeMismatch]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [authorDO.id,id]; arguments []; default message [id]]; default message [Failed to convert property value of type \'java.lang.String\' to required type \'java.lang.Long\' for property \'id\'; nested exception is java.lang.NumberFormatException: For input string: \"1,1\"]', null, '2020-05-13 14:14:47');
INSERT INTO `sys_log` VALUES ('1410', '1', 'admin', '登录', '261', 'com.java2nb.system.controller.LoginController.ajaxLogin()', null, '127.0.0.1', '2020-05-13 14:18:07');
INSERT INTO `sys_log` VALUES ('1411', '1', 'admin', '请求访问主页', '83', 'com.java2nb.system.controller.LoginController.index()', null, '127.0.0.1', '2020-05-13 14:18:07');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '基础管理', '', '', '0', 'fa fa-bars', '0', '2017-08-09 22:49:47', null);
INSERT INTO `sys_menu` VALUES ('2', '3', '系统菜单', 'sys/menu/', 'sys:menu:menu', '1', 'fa fa-th-list', '2', '2017-08-09 22:55:15', null);
INSERT INTO `sys_menu` VALUES ('3', '0', '系统管理', null, null, '0', 'fa fa-desktop', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('6', '3', '用户管理', 'sys/user/', 'sys:user:user', '1', 'fa fa-user', '0', '2017-08-10 14:12:11', null);
INSERT INTO `sys_menu` VALUES ('7', '3', '角色管理', 'sys/role', 'sys:role:role', '1', 'fa fa-paw', '1', '2017-08-10 14:13:19', null);
INSERT INTO `sys_menu` VALUES ('12', '6', '新增', '', 'sys:user:add', '2', '', '0', '2017-08-14 10:51:35', null);
INSERT INTO `sys_menu` VALUES ('13', '6', '编辑', '', 'sys:user:edit', '2', '', '0', '2017-08-14 10:52:06', null);
INSERT INTO `sys_menu` VALUES ('14', '6', '删除', null, 'sys:user:remove', '2', null, '0', '2017-08-14 10:52:24', null);
INSERT INTO `sys_menu` VALUES ('15', '7', '新增', '', 'sys:role:add', '2', '', '0', '2017-08-14 10:56:37', null);
INSERT INTO `sys_menu` VALUES ('20', '2', '新增', '', 'sys:menu:add', '2', '', '0', '2017-08-14 10:59:32', null);
INSERT INTO `sys_menu` VALUES ('21', '2', '编辑', '', 'sys:menu:edit', '2', '', '0', '2017-08-14 10:59:56', null);
INSERT INTO `sys_menu` VALUES ('22', '2', '删除', '', 'sys:menu:remove', '2', '', '0', '2017-08-14 11:00:26', null);
INSERT INTO `sys_menu` VALUES ('24', '6', '批量删除', '', 'sys:user:batchRemove', '2', '', '0', '2017-08-14 17:27:18', null);
INSERT INTO `sys_menu` VALUES ('25', '6', '停用', null, 'sys:user:disable', '2', null, '0', '2017-08-14 17:27:43', null);
INSERT INTO `sys_menu` VALUES ('26', '6', '重置密码', '', 'sys:user:resetPwd', '2', '', '0', '2017-08-14 17:28:34', null);
INSERT INTO `sys_menu` VALUES ('27', '91', '系统日志', 'common/log', 'common:log', '1', 'fa fa-warning', '0', '2017-08-14 22:11:53', null);
INSERT INTO `sys_menu` VALUES ('28', '27', '刷新', null, 'sys:log:list', '2', null, '0', '2017-08-14 22:30:22', null);
INSERT INTO `sys_menu` VALUES ('29', '27', '删除', null, 'sys:log:remove', '2', null, '0', '2017-08-14 22:30:43', null);
INSERT INTO `sys_menu` VALUES ('30', '27', '清空', null, 'sys:log:clear', '2', null, '0', '2017-08-14 22:31:02', null);
INSERT INTO `sys_menu` VALUES ('48', '77', '代码生成', 'common/generator', 'common:generator', '1', 'fa fa-code', '3', null, null);
INSERT INTO `sys_menu` VALUES ('55', '7', '编辑', '', 'sys:role:edit', '2', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('56', '7', '删除', '', 'sys:role:remove', '2', null, null, null, null);
INSERT INTO `sys_menu` VALUES ('57', '91', '运行监控', '/druid/index.html', '', '1', 'fa fa-caret-square-o-right', '1', null, null);
INSERT INTO `sys_menu` VALUES ('61', '2', '批量删除', '', 'sys:menu:batchRemove', '2', null, null, null, null);
INSERT INTO `sys_menu` VALUES ('62', '7', '批量删除', '', 'sys:role:batchRemove', '2', null, null, null, null);
INSERT INTO `sys_menu` VALUES ('71', '1', '文件管理', '/common/sysFile', 'common:sysFile:sysFile', '1', 'fa fa-folder-open', '2', null, null);
INSERT INTO `sys_menu` VALUES ('73', '3', '部门管理', '/system/sysDept', 'system:sysDept:sysDept', '1', 'fa fa-users', '3', null, null);
INSERT INTO `sys_menu` VALUES ('74', '73', '增加', '/system/sysDept/add', 'system:sysDept:add', '2', null, '1', null, null);
INSERT INTO `sys_menu` VALUES ('75', '73', '刪除', 'system/sysDept/remove', 'system:sysDept:remove', '2', null, '2', null, null);
INSERT INTO `sys_menu` VALUES ('76', '73', '编辑', '/system/sysDept/edit', 'system:sysDept:edit', '2', null, '3', null, null);
INSERT INTO `sys_menu` VALUES ('77', '0', '研发工具', '', '', '0', 'fa fa-gear', '5', null, null);
INSERT INTO `sys_menu` VALUES ('78', '1', '数据字典', '/common/dict', 'common:dict:dict', '1', 'fa fa-book', '1', null, null);
INSERT INTO `sys_menu` VALUES ('79', '78', '增加', '/common/dict/add', 'common:dict:add', '2', null, '2', null, null);
INSERT INTO `sys_menu` VALUES ('80', '78', '编辑', '/common/dict/edit', 'common:dict:edit', '2', null, '2', null, null);
INSERT INTO `sys_menu` VALUES ('81', '78', '删除', '/common/dict/remove', 'common:dict:remove', '2', '', '3', null, null);
INSERT INTO `sys_menu` VALUES ('83', '78', '批量删除', '/common/dict/batchRemove', 'common:dict:batchRemove', '2', '', '4', null, null);
INSERT INTO `sys_menu` VALUES ('91', '0', '系统监控', '', '', '0', 'fa fa-video-camera', '4', null, null);
INSERT INTO `sys_menu` VALUES ('92', '91', '在线用户', 'sys/online', '', '1', 'fa fa-user', null, null, null);
INSERT INTO `sys_menu` VALUES ('104', '77', 'swagger', '/swagger-ui.html', '', '1', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('202', '0', '测试管理', '', '', '0', 'fa fa-s15', '12', null, null);
INSERT INTO `sys_menu` VALUES ('203', '202', '订单管理', 'test/order', 'test:order:order', '1', '', '1', null, null);
INSERT INTO `sys_menu` VALUES ('204', '203', '新增', '', 'test:order:add', '2', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('205', '203', '编辑', '', 'test:order:edit', '2', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('206', '203', '删除', '', 'test:order:remove', '2', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('207', '203', '批量删除', '', 'test:order:batchRemove', '2', '', null, null, null);
INSERT INTO `sys_menu` VALUES ('208', '203', '详情', '', 'test:order:detail', '2', '', '0', null, null);
INSERT INTO `sys_menu` VALUES ('209', '3', '数据权限', 'system/dataPerm', 'system:dataPerm:dataPerm', '1', 'fa', '6', null, null);
INSERT INTO `sys_menu` VALUES ('210', '209', '查看', null, 'system:dataPerm:detail', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('211', '209', '新增', null, 'system:dataPerm:add', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('212', '209', '修改', null, 'system:dataPerm:edit', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('213', '209', '删除', null, 'system:dataPerm:remove', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('214', '209', '批量删除', null, 'system:dataPerm:batchRemove', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('221', '0', '作家管理', '', '', '0', 'fa fa-user-o', '10', null, null);
INSERT INTO `sys_menu` VALUES ('222', '221', '作者列表', 'novel/author', 'novel:author:author', '1', 'fa', '6', null, null);
INSERT INTO `sys_menu` VALUES ('223', '222', '查看', null, 'novel:author:detail', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('224', '222', '新增', null, 'novel:author:add', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('225', '222', '修改', null, 'novel:author:edit', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('226', '222', '删除', null, 'novel:author:remove', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('227', '222', '批量删除', null, 'novel:author:batchRemove', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('228', '221', '邀请码管理', 'novel/authorCode', 'novel:authorCode:authorCode', '1', 'fa', '3', null, null);
INSERT INTO `sys_menu` VALUES ('229', '228', '查看', null, 'novel:authorCode:detail', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('230', '228', '新增', null, 'novel:authorCode:add', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('231', '228', '修改', null, 'novel:authorCode:edit', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('232', '228', '删除', null, 'novel:authorCode:remove', '2', null, '6', null, null);
INSERT INTO `sys_menu` VALUES ('233', '228', '批量删除', null, 'novel:authorCode:batchRemove', '2', null, '6', null, null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(100) DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `user_id_create` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级用户角色', 'admin', '拥有最高权限', '2', '2017-08-12 00:43:52', '2017-08-12 19:14:59');
INSERT INTO `sys_role` VALUES ('59', '普通用户', null, '基本用户权限', null, null, null);
INSERT INTO `sys_role` VALUES ('60', '测试', null, '&lt;div&gt;', null, null, null);
INSERT INTO `sys_role` VALUES ('61', 'test', null, '测试', null, null, null);

-- ----------------------------
-- Table structure for sys_role_data_perm
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_data_perm`;
CREATE TABLE `sys_role_data_perm` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `perm_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8 COMMENT='角色与数据权限对应关系';

-- ----------------------------
-- Records of sys_role_data_perm
-- ----------------------------
INSERT INTO `sys_role_data_perm` VALUES ('60', '60', '211');
INSERT INTO `sys_role_data_perm` VALUES ('61', '60', '-1');
INSERT INTO `sys_role_data_perm` VALUES ('62', '60', '1199170283966787584');
INSERT INTO `sys_role_data_perm` VALUES ('71', '1', '214');
INSERT INTO `sys_role_data_perm` VALUES ('72', '1', '213');
INSERT INTO `sys_role_data_perm` VALUES ('73', '1', '212');
INSERT INTO `sys_role_data_perm` VALUES ('74', '1', '211');
INSERT INTO `sys_role_data_perm` VALUES ('75', '1', '210');
INSERT INTO `sys_role_data_perm` VALUES ('76', '1', '1260412100929482752');
INSERT INTO `sys_role_data_perm` VALUES ('77', '1', '-1');
INSERT INTO `sys_role_data_perm` VALUES ('78', '1', '1260412099998347264');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4830 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('367', '44', '1');
INSERT INTO `sys_role_menu` VALUES ('368', '44', '32');
INSERT INTO `sys_role_menu` VALUES ('369', '44', '33');
INSERT INTO `sys_role_menu` VALUES ('370', '44', '34');
INSERT INTO `sys_role_menu` VALUES ('371', '44', '35');
INSERT INTO `sys_role_menu` VALUES ('372', '44', '28');
INSERT INTO `sys_role_menu` VALUES ('373', '44', '29');
INSERT INTO `sys_role_menu` VALUES ('374', '44', '30');
INSERT INTO `sys_role_menu` VALUES ('375', '44', '38');
INSERT INTO `sys_role_menu` VALUES ('376', '44', '4');
INSERT INTO `sys_role_menu` VALUES ('377', '44', '27');
INSERT INTO `sys_role_menu` VALUES ('378', '45', '38');
INSERT INTO `sys_role_menu` VALUES ('379', '46', '3');
INSERT INTO `sys_role_menu` VALUES ('380', '46', '20');
INSERT INTO `sys_role_menu` VALUES ('381', '46', '21');
INSERT INTO `sys_role_menu` VALUES ('382', '46', '22');
INSERT INTO `sys_role_menu` VALUES ('383', '46', '23');
INSERT INTO `sys_role_menu` VALUES ('384', '46', '11');
INSERT INTO `sys_role_menu` VALUES ('385', '46', '12');
INSERT INTO `sys_role_menu` VALUES ('386', '46', '13');
INSERT INTO `sys_role_menu` VALUES ('387', '46', '14');
INSERT INTO `sys_role_menu` VALUES ('388', '46', '24');
INSERT INTO `sys_role_menu` VALUES ('389', '46', '25');
INSERT INTO `sys_role_menu` VALUES ('390', '46', '26');
INSERT INTO `sys_role_menu` VALUES ('391', '46', '15');
INSERT INTO `sys_role_menu` VALUES ('392', '46', '2');
INSERT INTO `sys_role_menu` VALUES ('393', '46', '6');
INSERT INTO `sys_role_menu` VALUES ('394', '46', '7');
INSERT INTO `sys_role_menu` VALUES ('598', '50', '38');
INSERT INTO `sys_role_menu` VALUES ('632', '38', '42');
INSERT INTO `sys_role_menu` VALUES ('737', '51', '38');
INSERT INTO `sys_role_menu` VALUES ('738', '51', '39');
INSERT INTO `sys_role_menu` VALUES ('739', '51', '40');
INSERT INTO `sys_role_menu` VALUES ('740', '51', '41');
INSERT INTO `sys_role_menu` VALUES ('741', '51', '4');
INSERT INTO `sys_role_menu` VALUES ('742', '51', '32');
INSERT INTO `sys_role_menu` VALUES ('743', '51', '33');
INSERT INTO `sys_role_menu` VALUES ('744', '51', '34');
INSERT INTO `sys_role_menu` VALUES ('745', '51', '35');
INSERT INTO `sys_role_menu` VALUES ('746', '51', '27');
INSERT INTO `sys_role_menu` VALUES ('747', '51', '28');
INSERT INTO `sys_role_menu` VALUES ('748', '51', '29');
INSERT INTO `sys_role_menu` VALUES ('749', '51', '30');
INSERT INTO `sys_role_menu` VALUES ('750', '51', '1');
INSERT INTO `sys_role_menu` VALUES ('1064', '54', '53');
INSERT INTO `sys_role_menu` VALUES ('1095', '55', '2');
INSERT INTO `sys_role_menu` VALUES ('1096', '55', '6');
INSERT INTO `sys_role_menu` VALUES ('1097', '55', '7');
INSERT INTO `sys_role_menu` VALUES ('1098', '55', '3');
INSERT INTO `sys_role_menu` VALUES ('1099', '55', '50');
INSERT INTO `sys_role_menu` VALUES ('1100', '55', '49');
INSERT INTO `sys_role_menu` VALUES ('1101', '55', '1');
INSERT INTO `sys_role_menu` VALUES ('1856', '53', '28');
INSERT INTO `sys_role_menu` VALUES ('1857', '53', '29');
INSERT INTO `sys_role_menu` VALUES ('1858', '53', '30');
INSERT INTO `sys_role_menu` VALUES ('1859', '53', '27');
INSERT INTO `sys_role_menu` VALUES ('1860', '53', '57');
INSERT INTO `sys_role_menu` VALUES ('1861', '53', '71');
INSERT INTO `sys_role_menu` VALUES ('1862', '53', '48');
INSERT INTO `sys_role_menu` VALUES ('1863', '53', '72');
INSERT INTO `sys_role_menu` VALUES ('1864', '53', '1');
INSERT INTO `sys_role_menu` VALUES ('1865', '53', '7');
INSERT INTO `sys_role_menu` VALUES ('1866', '53', '55');
INSERT INTO `sys_role_menu` VALUES ('1867', '53', '56');
INSERT INTO `sys_role_menu` VALUES ('1868', '53', '62');
INSERT INTO `sys_role_menu` VALUES ('1869', '53', '15');
INSERT INTO `sys_role_menu` VALUES ('1870', '53', '2');
INSERT INTO `sys_role_menu` VALUES ('1871', '53', '61');
INSERT INTO `sys_role_menu` VALUES ('1872', '53', '20');
INSERT INTO `sys_role_menu` VALUES ('1873', '53', '21');
INSERT INTO `sys_role_menu` VALUES ('1874', '53', '22');
INSERT INTO `sys_role_menu` VALUES ('2084', '56', '68');
INSERT INTO `sys_role_menu` VALUES ('2085', '56', '60');
INSERT INTO `sys_role_menu` VALUES ('2086', '56', '59');
INSERT INTO `sys_role_menu` VALUES ('2087', '56', '58');
INSERT INTO `sys_role_menu` VALUES ('2088', '56', '51');
INSERT INTO `sys_role_menu` VALUES ('2089', '56', '50');
INSERT INTO `sys_role_menu` VALUES ('2090', '56', '49');
INSERT INTO `sys_role_menu` VALUES ('2243', '48', '72');
INSERT INTO `sys_role_menu` VALUES ('2247', '63', '-1');
INSERT INTO `sys_role_menu` VALUES ('2248', '63', '84');
INSERT INTO `sys_role_menu` VALUES ('2249', '63', '85');
INSERT INTO `sys_role_menu` VALUES ('2250', '63', '88');
INSERT INTO `sys_role_menu` VALUES ('2251', '63', '87');
INSERT INTO `sys_role_menu` VALUES ('2252', '64', '84');
INSERT INTO `sys_role_menu` VALUES ('2253', '64', '89');
INSERT INTO `sys_role_menu` VALUES ('2254', '64', '88');
INSERT INTO `sys_role_menu` VALUES ('2255', '64', '87');
INSERT INTO `sys_role_menu` VALUES ('2256', '64', '86');
INSERT INTO `sys_role_menu` VALUES ('2257', '64', '85');
INSERT INTO `sys_role_menu` VALUES ('2258', '65', '89');
INSERT INTO `sys_role_menu` VALUES ('2259', '65', '88');
INSERT INTO `sys_role_menu` VALUES ('2260', '65', '86');
INSERT INTO `sys_role_menu` VALUES ('2262', '67', '48');
INSERT INTO `sys_role_menu` VALUES ('2263', '68', '88');
INSERT INTO `sys_role_menu` VALUES ('2264', '68', '87');
INSERT INTO `sys_role_menu` VALUES ('2265', '69', '89');
INSERT INTO `sys_role_menu` VALUES ('2266', '69', '88');
INSERT INTO `sys_role_menu` VALUES ('2267', '69', '86');
INSERT INTO `sys_role_menu` VALUES ('2268', '69', '87');
INSERT INTO `sys_role_menu` VALUES ('2269', '69', '85');
INSERT INTO `sys_role_menu` VALUES ('2270', '69', '84');
INSERT INTO `sys_role_menu` VALUES ('2271', '70', '85');
INSERT INTO `sys_role_menu` VALUES ('2272', '70', '89');
INSERT INTO `sys_role_menu` VALUES ('2273', '70', '88');
INSERT INTO `sys_role_menu` VALUES ('2274', '70', '87');
INSERT INTO `sys_role_menu` VALUES ('2275', '70', '86');
INSERT INTO `sys_role_menu` VALUES ('2276', '70', '84');
INSERT INTO `sys_role_menu` VALUES ('2277', '71', '87');
INSERT INTO `sys_role_menu` VALUES ('2278', '72', '59');
INSERT INTO `sys_role_menu` VALUES ('2279', '73', '48');
INSERT INTO `sys_role_menu` VALUES ('2280', '74', '88');
INSERT INTO `sys_role_menu` VALUES ('2281', '74', '87');
INSERT INTO `sys_role_menu` VALUES ('2282', '75', '88');
INSERT INTO `sys_role_menu` VALUES ('2283', '75', '87');
INSERT INTO `sys_role_menu` VALUES ('2284', '76', '85');
INSERT INTO `sys_role_menu` VALUES ('2285', '76', '89');
INSERT INTO `sys_role_menu` VALUES ('2286', '76', '88');
INSERT INTO `sys_role_menu` VALUES ('2287', '76', '87');
INSERT INTO `sys_role_menu` VALUES ('2288', '76', '86');
INSERT INTO `sys_role_menu` VALUES ('2289', '76', '84');
INSERT INTO `sys_role_menu` VALUES ('2292', '78', '88');
INSERT INTO `sys_role_menu` VALUES ('2293', '78', '87');
INSERT INTO `sys_role_menu` VALUES ('2294', '78', null);
INSERT INTO `sys_role_menu` VALUES ('2295', '78', null);
INSERT INTO `sys_role_menu` VALUES ('2296', '78', null);
INSERT INTO `sys_role_menu` VALUES ('2308', '80', '87');
INSERT INTO `sys_role_menu` VALUES ('2309', '80', '86');
INSERT INTO `sys_role_menu` VALUES ('2310', '80', '-1');
INSERT INTO `sys_role_menu` VALUES ('2311', '80', '84');
INSERT INTO `sys_role_menu` VALUES ('2312', '80', '85');
INSERT INTO `sys_role_menu` VALUES ('2328', '79', '72');
INSERT INTO `sys_role_menu` VALUES ('2329', '79', '48');
INSERT INTO `sys_role_menu` VALUES ('2330', '79', '77');
INSERT INTO `sys_role_menu` VALUES ('2331', '79', '84');
INSERT INTO `sys_role_menu` VALUES ('2332', '79', '89');
INSERT INTO `sys_role_menu` VALUES ('2333', '79', '88');
INSERT INTO `sys_role_menu` VALUES ('2334', '79', '87');
INSERT INTO `sys_role_menu` VALUES ('2335', '79', '86');
INSERT INTO `sys_role_menu` VALUES ('2336', '79', '85');
INSERT INTO `sys_role_menu` VALUES ('2337', '79', '-1');
INSERT INTO `sys_role_menu` VALUES ('2338', '77', '89');
INSERT INTO `sys_role_menu` VALUES ('2339', '77', '88');
INSERT INTO `sys_role_menu` VALUES ('2340', '77', '87');
INSERT INTO `sys_role_menu` VALUES ('2341', '77', '86');
INSERT INTO `sys_role_menu` VALUES ('2342', '77', '85');
INSERT INTO `sys_role_menu` VALUES ('2343', '77', '84');
INSERT INTO `sys_role_menu` VALUES ('2344', '77', '72');
INSERT INTO `sys_role_menu` VALUES ('2345', '77', '-1');
INSERT INTO `sys_role_menu` VALUES ('2346', '77', '77');
INSERT INTO `sys_role_menu` VALUES ('2974', '57', '93');
INSERT INTO `sys_role_menu` VALUES ('2975', '57', '99');
INSERT INTO `sys_role_menu` VALUES ('2976', '57', '95');
INSERT INTO `sys_role_menu` VALUES ('2977', '57', '101');
INSERT INTO `sys_role_menu` VALUES ('2978', '57', '96');
INSERT INTO `sys_role_menu` VALUES ('2979', '57', '94');
INSERT INTO `sys_role_menu` VALUES ('2980', '57', '-1');
INSERT INTO `sys_role_menu` VALUES ('2981', '58', '93');
INSERT INTO `sys_role_menu` VALUES ('2982', '58', '99');
INSERT INTO `sys_role_menu` VALUES ('2983', '58', '95');
INSERT INTO `sys_role_menu` VALUES ('2984', '58', '101');
INSERT INTO `sys_role_menu` VALUES ('2985', '58', '96');
INSERT INTO `sys_role_menu` VALUES ('2986', '58', '94');
INSERT INTO `sys_role_menu` VALUES ('2987', '58', '-1');
INSERT INTO `sys_role_menu` VALUES ('3232', '59', '98');
INSERT INTO `sys_role_menu` VALUES ('3233', '59', '101');
INSERT INTO `sys_role_menu` VALUES ('3234', '59', '99');
INSERT INTO `sys_role_menu` VALUES ('3235', '59', '95');
INSERT INTO `sys_role_menu` VALUES ('3236', '59', '90');
INSERT INTO `sys_role_menu` VALUES ('3237', '59', '89');
INSERT INTO `sys_role_menu` VALUES ('3238', '59', '88');
INSERT INTO `sys_role_menu` VALUES ('3239', '59', '87');
INSERT INTO `sys_role_menu` VALUES ('3240', '59', '86');
INSERT INTO `sys_role_menu` VALUES ('3241', '59', '68');
INSERT INTO `sys_role_menu` VALUES ('3242', '59', '60');
INSERT INTO `sys_role_menu` VALUES ('3243', '59', '59');
INSERT INTO `sys_role_menu` VALUES ('3244', '59', '58');
INSERT INTO `sys_role_menu` VALUES ('3245', '59', '51');
INSERT INTO `sys_role_menu` VALUES ('3246', '59', '76');
INSERT INTO `sys_role_menu` VALUES ('3247', '59', '75');
INSERT INTO `sys_role_menu` VALUES ('3248', '59', '74');
INSERT INTO `sys_role_menu` VALUES ('3249', '59', '62');
INSERT INTO `sys_role_menu` VALUES ('3250', '59', '56');
INSERT INTO `sys_role_menu` VALUES ('3251', '59', '55');
INSERT INTO `sys_role_menu` VALUES ('3252', '59', '15');
INSERT INTO `sys_role_menu` VALUES ('3253', '59', '26');
INSERT INTO `sys_role_menu` VALUES ('3254', '59', '25');
INSERT INTO `sys_role_menu` VALUES ('3255', '59', '24');
INSERT INTO `sys_role_menu` VALUES ('3256', '59', '14');
INSERT INTO `sys_role_menu` VALUES ('3257', '59', '13');
INSERT INTO `sys_role_menu` VALUES ('3258', '59', '12');
INSERT INTO `sys_role_menu` VALUES ('3259', '59', '61');
INSERT INTO `sys_role_menu` VALUES ('3260', '59', '22');
INSERT INTO `sys_role_menu` VALUES ('3261', '59', '21');
INSERT INTO `sys_role_menu` VALUES ('3262', '59', '20');
INSERT INTO `sys_role_menu` VALUES ('3263', '59', '83');
INSERT INTO `sys_role_menu` VALUES ('3264', '59', '81');
INSERT INTO `sys_role_menu` VALUES ('3265', '59', '80');
INSERT INTO `sys_role_menu` VALUES ('3266', '59', '79');
INSERT INTO `sys_role_menu` VALUES ('3267', '59', '71');
INSERT INTO `sys_role_menu` VALUES ('3268', '59', '97');
INSERT INTO `sys_role_menu` VALUES ('3269', '59', '96');
INSERT INTO `sys_role_menu` VALUES ('3270', '59', '94');
INSERT INTO `sys_role_menu` VALUES ('3271', '59', '93');
INSERT INTO `sys_role_menu` VALUES ('3272', '59', '85');
INSERT INTO `sys_role_menu` VALUES ('3273', '59', '84');
INSERT INTO `sys_role_menu` VALUES ('3274', '59', '50');
INSERT INTO `sys_role_menu` VALUES ('3275', '59', '49');
INSERT INTO `sys_role_menu` VALUES ('3276', '59', '73');
INSERT INTO `sys_role_menu` VALUES ('3277', '59', '7');
INSERT INTO `sys_role_menu` VALUES ('3278', '59', '6');
INSERT INTO `sys_role_menu` VALUES ('3279', '59', '2');
INSERT INTO `sys_role_menu` VALUES ('3280', '59', '3');
INSERT INTO `sys_role_menu` VALUES ('3281', '59', '78');
INSERT INTO `sys_role_menu` VALUES ('3282', '59', '1');
INSERT INTO `sys_role_menu` VALUES ('3283', '59', '-1');
INSERT INTO `sys_role_menu` VALUES ('4611', '61', '208');
INSERT INTO `sys_role_menu` VALUES ('4612', '61', '207');
INSERT INTO `sys_role_menu` VALUES ('4613', '61', '206');
INSERT INTO `sys_role_menu` VALUES ('4614', '61', '205');
INSERT INTO `sys_role_menu` VALUES ('4615', '61', '204');
INSERT INTO `sys_role_menu` VALUES ('4616', '61', '92');
INSERT INTO `sys_role_menu` VALUES ('4617', '61', '57');
INSERT INTO `sys_role_menu` VALUES ('4618', '61', '30');
INSERT INTO `sys_role_menu` VALUES ('4619', '61', '29');
INSERT INTO `sys_role_menu` VALUES ('4620', '61', '28');
INSERT INTO `sys_role_menu` VALUES ('4621', '61', '104');
INSERT INTO `sys_role_menu` VALUES ('4622', '61', '48');
INSERT INTO `sys_role_menu` VALUES ('4623', '61', '214');
INSERT INTO `sys_role_menu` VALUES ('4624', '61', '213');
INSERT INTO `sys_role_menu` VALUES ('4625', '61', '212');
INSERT INTO `sys_role_menu` VALUES ('4626', '61', '211');
INSERT INTO `sys_role_menu` VALUES ('4627', '61', '210');
INSERT INTO `sys_role_menu` VALUES ('4628', '61', '76');
INSERT INTO `sys_role_menu` VALUES ('4629', '61', '75');
INSERT INTO `sys_role_menu` VALUES ('4630', '61', '74');
INSERT INTO `sys_role_menu` VALUES ('4631', '61', '62');
INSERT INTO `sys_role_menu` VALUES ('4632', '61', '56');
INSERT INTO `sys_role_menu` VALUES ('4633', '61', '55');
INSERT INTO `sys_role_menu` VALUES ('4634', '61', '15');
INSERT INTO `sys_role_menu` VALUES ('4635', '61', '26');
INSERT INTO `sys_role_menu` VALUES ('4636', '61', '25');
INSERT INTO `sys_role_menu` VALUES ('4637', '61', '24');
INSERT INTO `sys_role_menu` VALUES ('4638', '61', '14');
INSERT INTO `sys_role_menu` VALUES ('4639', '61', '13');
INSERT INTO `sys_role_menu` VALUES ('4640', '61', '12');
INSERT INTO `sys_role_menu` VALUES ('4641', '61', '61');
INSERT INTO `sys_role_menu` VALUES ('4642', '61', '22');
INSERT INTO `sys_role_menu` VALUES ('4643', '61', '21');
INSERT INTO `sys_role_menu` VALUES ('4644', '61', '20');
INSERT INTO `sys_role_menu` VALUES ('4645', '61', '83');
INSERT INTO `sys_role_menu` VALUES ('4646', '61', '81');
INSERT INTO `sys_role_menu` VALUES ('4647', '61', '80');
INSERT INTO `sys_role_menu` VALUES ('4648', '61', '79');
INSERT INTO `sys_role_menu` VALUES ('4649', '61', '71');
INSERT INTO `sys_role_menu` VALUES ('4650', '61', '203');
INSERT INTO `sys_role_menu` VALUES ('4651', '61', '202');
INSERT INTO `sys_role_menu` VALUES ('4652', '61', '27');
INSERT INTO `sys_role_menu` VALUES ('4653', '61', '91');
INSERT INTO `sys_role_menu` VALUES ('4654', '61', '77');
INSERT INTO `sys_role_menu` VALUES ('4655', '61', '209');
INSERT INTO `sys_role_menu` VALUES ('4656', '61', '73');
INSERT INTO `sys_role_menu` VALUES ('4657', '61', '7');
INSERT INTO `sys_role_menu` VALUES ('4658', '61', '6');
INSERT INTO `sys_role_menu` VALUES ('4659', '61', '2');
INSERT INTO `sys_role_menu` VALUES ('4660', '61', '3');
INSERT INTO `sys_role_menu` VALUES ('4661', '61', '78');
INSERT INTO `sys_role_menu` VALUES ('4662', '61', '1');
INSERT INTO `sys_role_menu` VALUES ('4663', '61', '-1');
INSERT INTO `sys_role_menu` VALUES ('4664', '60', '92');
INSERT INTO `sys_role_menu` VALUES ('4665', '60', '57');
INSERT INTO `sys_role_menu` VALUES ('4666', '60', '30');
INSERT INTO `sys_role_menu` VALUES ('4667', '60', '29');
INSERT INTO `sys_role_menu` VALUES ('4668', '60', '28');
INSERT INTO `sys_role_menu` VALUES ('4669', '60', '104');
INSERT INTO `sys_role_menu` VALUES ('4670', '60', '48');
INSERT INTO `sys_role_menu` VALUES ('4671', '60', '76');
INSERT INTO `sys_role_menu` VALUES ('4672', '60', '75');
INSERT INTO `sys_role_menu` VALUES ('4673', '60', '74');
INSERT INTO `sys_role_menu` VALUES ('4674', '60', '62');
INSERT INTO `sys_role_menu` VALUES ('4675', '60', '56');
INSERT INTO `sys_role_menu` VALUES ('4676', '60', '55');
INSERT INTO `sys_role_menu` VALUES ('4677', '60', '15');
INSERT INTO `sys_role_menu` VALUES ('4678', '60', '26');
INSERT INTO `sys_role_menu` VALUES ('4679', '60', '25');
INSERT INTO `sys_role_menu` VALUES ('4680', '60', '24');
INSERT INTO `sys_role_menu` VALUES ('4681', '60', '14');
INSERT INTO `sys_role_menu` VALUES ('4682', '60', '13');
INSERT INTO `sys_role_menu` VALUES ('4683', '60', '12');
INSERT INTO `sys_role_menu` VALUES ('4684', '60', '61');
INSERT INTO `sys_role_menu` VALUES ('4685', '60', '22');
INSERT INTO `sys_role_menu` VALUES ('4686', '60', '21');
INSERT INTO `sys_role_menu` VALUES ('4687', '60', '20');
INSERT INTO `sys_role_menu` VALUES ('4688', '60', '83');
INSERT INTO `sys_role_menu` VALUES ('4689', '60', '81');
INSERT INTO `sys_role_menu` VALUES ('4690', '60', '80');
INSERT INTO `sys_role_menu` VALUES ('4691', '60', '79');
INSERT INTO `sys_role_menu` VALUES ('4692', '60', '71');
INSERT INTO `sys_role_menu` VALUES ('4693', '60', '27');
INSERT INTO `sys_role_menu` VALUES ('4694', '60', '91');
INSERT INTO `sys_role_menu` VALUES ('4695', '60', '77');
INSERT INTO `sys_role_menu` VALUES ('4696', '60', '73');
INSERT INTO `sys_role_menu` VALUES ('4697', '60', '7');
INSERT INTO `sys_role_menu` VALUES ('4698', '60', '6');
INSERT INTO `sys_role_menu` VALUES ('4699', '60', '2');
INSERT INTO `sys_role_menu` VALUES ('4700', '60', '78');
INSERT INTO `sys_role_menu` VALUES ('4701', '60', '1');
INSERT INTO `sys_role_menu` VALUES ('4702', '60', '-1');
INSERT INTO `sys_role_menu` VALUES ('4703', '60', '3');
INSERT INTO `sys_role_menu` VALUES ('4764', '1', '227');
INSERT INTO `sys_role_menu` VALUES ('4765', '1', '226');
INSERT INTO `sys_role_menu` VALUES ('4766', '1', '225');
INSERT INTO `sys_role_menu` VALUES ('4767', '1', '224');
INSERT INTO `sys_role_menu` VALUES ('4768', '1', '223');
INSERT INTO `sys_role_menu` VALUES ('4769', '1', '208');
INSERT INTO `sys_role_menu` VALUES ('4770', '1', '207');
INSERT INTO `sys_role_menu` VALUES ('4771', '1', '206');
INSERT INTO `sys_role_menu` VALUES ('4772', '1', '205');
INSERT INTO `sys_role_menu` VALUES ('4773', '1', '204');
INSERT INTO `sys_role_menu` VALUES ('4774', '1', '92');
INSERT INTO `sys_role_menu` VALUES ('4775', '1', '57');
INSERT INTO `sys_role_menu` VALUES ('4776', '1', '30');
INSERT INTO `sys_role_menu` VALUES ('4777', '1', '29');
INSERT INTO `sys_role_menu` VALUES ('4778', '1', '28');
INSERT INTO `sys_role_menu` VALUES ('4779', '1', '104');
INSERT INTO `sys_role_menu` VALUES ('4780', '1', '48');
INSERT INTO `sys_role_menu` VALUES ('4781', '1', '214');
INSERT INTO `sys_role_menu` VALUES ('4782', '1', '213');
INSERT INTO `sys_role_menu` VALUES ('4783', '1', '212');
INSERT INTO `sys_role_menu` VALUES ('4784', '1', '211');
INSERT INTO `sys_role_menu` VALUES ('4785', '1', '210');
INSERT INTO `sys_role_menu` VALUES ('4786', '1', '76');
INSERT INTO `sys_role_menu` VALUES ('4787', '1', '75');
INSERT INTO `sys_role_menu` VALUES ('4788', '1', '74');
INSERT INTO `sys_role_menu` VALUES ('4789', '1', '62');
INSERT INTO `sys_role_menu` VALUES ('4790', '1', '56');
INSERT INTO `sys_role_menu` VALUES ('4791', '1', '55');
INSERT INTO `sys_role_menu` VALUES ('4792', '1', '15');
INSERT INTO `sys_role_menu` VALUES ('4793', '1', '26');
INSERT INTO `sys_role_menu` VALUES ('4794', '1', '25');
INSERT INTO `sys_role_menu` VALUES ('4795', '1', '24');
INSERT INTO `sys_role_menu` VALUES ('4796', '1', '14');
INSERT INTO `sys_role_menu` VALUES ('4797', '1', '13');
INSERT INTO `sys_role_menu` VALUES ('4798', '1', '12');
INSERT INTO `sys_role_menu` VALUES ('4799', '1', '61');
INSERT INTO `sys_role_menu` VALUES ('4800', '1', '22');
INSERT INTO `sys_role_menu` VALUES ('4801', '1', '21');
INSERT INTO `sys_role_menu` VALUES ('4802', '1', '20');
INSERT INTO `sys_role_menu` VALUES ('4803', '1', '83');
INSERT INTO `sys_role_menu` VALUES ('4804', '1', '81');
INSERT INTO `sys_role_menu` VALUES ('4805', '1', '80');
INSERT INTO `sys_role_menu` VALUES ('4806', '1', '79');
INSERT INTO `sys_role_menu` VALUES ('4807', '1', '71');
INSERT INTO `sys_role_menu` VALUES ('4808', '1', '222');
INSERT INTO `sys_role_menu` VALUES ('4809', '1', '203');
INSERT INTO `sys_role_menu` VALUES ('4810', '1', '202');
INSERT INTO `sys_role_menu` VALUES ('4811', '1', '27');
INSERT INTO `sys_role_menu` VALUES ('4812', '1', '91');
INSERT INTO `sys_role_menu` VALUES ('4813', '1', '77');
INSERT INTO `sys_role_menu` VALUES ('4814', '1', '209');
INSERT INTO `sys_role_menu` VALUES ('4815', '1', '73');
INSERT INTO `sys_role_menu` VALUES ('4816', '1', '7');
INSERT INTO `sys_role_menu` VALUES ('4817', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('4818', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('4819', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('4820', '1', '78');
INSERT INTO `sys_role_menu` VALUES ('4821', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('4822', '1', '228');
INSERT INTO `sys_role_menu` VALUES ('4823', '1', '233');
INSERT INTO `sys_role_menu` VALUES ('4824', '1', '232');
INSERT INTO `sys_role_menu` VALUES ('4825', '1', '231');
INSERT INTO `sys_role_menu` VALUES ('4826', '1', '230');
INSERT INTO `sys_role_menu` VALUES ('4827', '1', '229');
INSERT INTO `sys_role_menu` VALUES ('4828', '1', '221');
INSERT INTO `sys_role_menu` VALUES ('4829', '1', '-1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `dept_id` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0:禁用，1:正常',
  `user_id_create` bigint(20) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `sex` bigint(20) DEFAULT NULL COMMENT '性别',
  `birth` datetime DEFAULT NULL COMMENT '出身日期',
  `pic_id` bigint(20) DEFAULT NULL,
  `live_address` varchar(500) DEFAULT NULL COMMENT '现居住地',
  `hobby` varchar(255) DEFAULT NULL COMMENT '爱好',
  `province` varchar(255) DEFAULT NULL COMMENT '省份',
  `city` varchar(255) DEFAULT NULL COMMENT '所在城市',
  `district` varchar(255) DEFAULT NULL COMMENT '所在地区',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '超级管理员', 'd633268afedf209e1e4ea0f5f43228a8', '6', 'admin@example.com', '17699999999', '1', '1', '2017-08-15 21:40:39', '2017-08-15 21:41:00', '96', '2017-12-14 00:00:00', '148', 'ccc', '122;121;', '北京市', '北京市市辖区', '东城区');
INSERT INTO `sys_user` VALUES ('2', 'test', '临时用户', 'd0af8fa1272ef5a152d9e27763eea293', '6', 'test@bootdo.com', null, '1', '1', '2017-08-14 13:43:05', '2017-08-14 21:15:36', null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('36', 'ldh', '刘德华', 'bfd9394475754fbe45866eba97738c36', '7', 'ldh@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('123', 'zxy', '张学友', '35174ba93f5fe7267f1fb3c1bf903781', '6', 'zxy@bootdo', null, '0', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('124', 'wyf', '吴亦凡', 'e179e6f687bbd57b9d7efc4746c8090a', '6', 'wyf@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('130', 'lh', '鹿晗', '7924710cd673f68967cde70e188bb097', '9', 'lh@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('131', 'lhc', '令狐冲', 'd515538e17ecb570ba40344b5618f5d4', '6', 'lhc@bootdo.com', null, '0', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('132', 'lyf', '刘亦菲', '7fdb1d9008f45950c1620ba0864e5fbd', '13', 'lyf@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('134', 'lyh', '李彦宏', 'dc26092b3244d9d432863f2738180e19', '8', 'lyh@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('135', 'wjl', '王健林', '3967697dfced162cf6a34080259b83aa', '6', 'wjl@bootod.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('136', 'gdg', '郭德纲', '3bb1bda86bc02bf6478cd91e42135d2f', '9', 'gdg@bootdo.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('137', 'test2', 'test2', '649169898e69272c0e5bc899baf1e904', null, '1179705413@qq.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `sys_user` VALUES ('138', 'test3', 'test3', '79ba2d0b58d8a2e94f6b18744c8cd280', '16', '1179705413@qq.com', null, '1', null, null, null, null, null, null, null, null, null, null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('73', '30', '48');
INSERT INTO `sys_user_role` VALUES ('74', '30', '49');
INSERT INTO `sys_user_role` VALUES ('75', '30', '50');
INSERT INTO `sys_user_role` VALUES ('76', '31', '48');
INSERT INTO `sys_user_role` VALUES ('77', '31', '49');
INSERT INTO `sys_user_role` VALUES ('78', '31', '52');
INSERT INTO `sys_user_role` VALUES ('79', '32', '48');
INSERT INTO `sys_user_role` VALUES ('80', '32', '49');
INSERT INTO `sys_user_role` VALUES ('81', '32', '50');
INSERT INTO `sys_user_role` VALUES ('82', '32', '51');
INSERT INTO `sys_user_role` VALUES ('83', '32', '52');
INSERT INTO `sys_user_role` VALUES ('84', '33', '38');
INSERT INTO `sys_user_role` VALUES ('85', '33', '49');
INSERT INTO `sys_user_role` VALUES ('86', '33', '52');
INSERT INTO `sys_user_role` VALUES ('87', '34', '50');
INSERT INTO `sys_user_role` VALUES ('88', '34', '51');
INSERT INTO `sys_user_role` VALUES ('89', '34', '52');
INSERT INTO `sys_user_role` VALUES ('106', '124', '1');
INSERT INTO `sys_user_role` VALUES ('110', '1', '1');
INSERT INTO `sys_user_role` VALUES ('111', '2', '1');
INSERT INTO `sys_user_role` VALUES ('113', '131', '48');
INSERT INTO `sys_user_role` VALUES ('117', '135', '1');
INSERT INTO `sys_user_role` VALUES ('120', '134', '1');
INSERT INTO `sys_user_role` VALUES ('121', '134', '48');
INSERT INTO `sys_user_role` VALUES ('123', '130', '1');
INSERT INTO `sys_user_role` VALUES ('124', null, '48');
INSERT INTO `sys_user_role` VALUES ('125', '132', '52');
INSERT INTO `sys_user_role` VALUES ('126', '132', '49');
INSERT INTO `sys_user_role` VALUES ('127', '123', '48');
INSERT INTO `sys_user_role` VALUES ('132', '36', '48');
INSERT INTO `sys_user_role` VALUES ('133', '137', '61');
INSERT INTO `sys_user_role` VALUES ('134', '137', '60');
INSERT INTO `sys_user_role` VALUES ('135', '138', '61');
INSERT INTO `sys_user_role` VALUES ('136', '138', '60');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '登录名',
  `password` varchar(100) NOT NULL COMMENT '登录密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `user_photo` varchar(100) DEFAULT NULL COMMENT '用户头像',
  `user_sex` tinyint(1) DEFAULT NULL COMMENT '用户性别，0：男，1：女',
  `account_balance` bigint(20) NOT NULL DEFAULT '0' COMMENT '账户余额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户状态，0：正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1255664783722586113 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1255060328322027520', '13560421324', 'e10adc3949ba59abbe56e057f20f883e', '13560421324', null, null, '0', '0', '2020-04-28 17:04:35', '2020-04-28 17:04:35');
INSERT INTO `user` VALUES ('1255379610071322624', '15924184378', '6a204bd89f3c8348afd5c77c717a097a', '15924184378', null, null, '0', '0', '2020-04-29 14:13:18', '2020-04-29 14:13:18');
INSERT INTO `user` VALUES ('1255396367099031552', '13111111111', 'a4de053ee1e8ba473312b537bc360709', '13111111111', null, null, '0', '0', '2020-04-29 15:19:53', '2020-04-29 15:19:53');
INSERT INTO `user` VALUES ('1255398795835895808', '13333333333', 'a4de053ee1e8ba473312b537bc360709', '13333333333', null, null, '0', '0', '2020-04-29 15:29:32', '2020-04-29 15:29:32');
INSERT INTO `user` VALUES ('1255403074344747008', '13444444444', 'a4de053ee1e8ba473312b537bc360709', '13444444444', null, null, '0', '0', '2020-04-29 15:46:32', '2020-04-29 15:46:32');
INSERT INTO `user` VALUES ('1255426058765852672', '13555555555', 'a4de053ee1e8ba473312b537bc360709', '13555555555', null, null, '0', '0', '2020-04-29 17:17:52', '2020-04-29 17:17:52');
INSERT INTO `user` VALUES ('1255664783722586112', '13560421323', 'e10adc3949ba59abbe56e057f20f883e', '13560421323', null, null, '0', '0', '2020-04-30 09:06:28', '2020-04-30 09:06:28');

-- ----------------------------
-- Table structure for user_bookshelf
-- ----------------------------
DROP TABLE IF EXISTS `user_bookshelf`;
CREATE TABLE `user_bookshelf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '小说ID',
  `pre_content_id` bigint(20) DEFAULT NULL COMMENT '上一次阅读的章节内容表ID',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_userid_bookid` (`user_id`,`book_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COMMENT='用户书架表';

-- ----------------------------
-- Records of user_bookshelf
-- ----------------------------
INSERT INTO `user_bookshelf` VALUES ('37', '1255060328322027520', '1254957312633352192', '3335449', '2020-04-30 07:27:23', '2020-04-30 19:37:36');
INSERT INTO `user_bookshelf` VALUES ('38', '1255664783722586112', '1254674396451897344', '1254674396690972672', '2020-04-30 09:06:53', '2020-04-30 09:06:59');
INSERT INTO `user_bookshelf` VALUES ('39', '1255060328322027520', '1254681071191785472', '1254681071552495616', '2020-04-30 09:37:47', null);
INSERT INTO `user_bookshelf` VALUES ('40', '1255060328322027520', '1254676970567565312', '3264258', '2020-04-30 09:57:18', '2020-04-30 19:19:11');
INSERT INTO `user_bookshelf` VALUES ('41', '1255060328322027520', '1254675594315759616', '1254675594496114688', '2020-04-30 18:37:18', null);

-- ----------------------------
-- Table structure for user_buy_record
-- ----------------------------
DROP TABLE IF EXISTS `user_buy_record`;
CREATE TABLE `user_buy_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) DEFAULT NULL COMMENT '购买的小说ID',
  `book_name` varchar(50) DEFAULT NULL COMMENT '购买的小说名',
  `book_index_id` bigint(20) DEFAULT NULL COMMENT '购买的章节ID',
  `book_index_name` varchar(100) DEFAULT NULL COMMENT '购买的章节名',
  `buy_amount` int(11) DEFAULT NULL COMMENT '购买使用的屋币数量',
  `create_time` datetime DEFAULT NULL COMMENT '购买时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_userId_indexId` (`user_id`,`book_index_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户消费记录表';

-- ----------------------------
-- Records of user_buy_record
-- ----------------------------
INSERT INTO `user_buy_record` VALUES ('1', '1255060328322027520', '1260400284744613890', '我是一只消消乐2', '1260522024606953472', '第三章', '10', '2020-05-13 21:29:09');
INSERT INTO `user_buy_record` VALUES ('2', '1255060328322027520', '1260400284744613890', '我是一只消消乐2', '1260564410687107072', '第四章', '10', '2020-05-13 21:40:38');

-- ----------------------------
-- Table structure for user_feedback
-- ----------------------------
DROP TABLE IF EXISTS `user_feedback`;
CREATE TABLE `user_feedback` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `content` varchar(512) DEFAULT NULL COMMENT '反馈内容',
  `create_time` datetime DEFAULT NULL COMMENT '反馈时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_feedback
-- ----------------------------
INSERT INTO `user_feedback` VALUES ('8', '1255060328322027520', '好战，多点书', '2020-04-30 08:58:49');

-- ----------------------------
-- Table structure for user_read_history
-- ----------------------------
DROP TABLE IF EXISTS `user_read_history`;
CREATE TABLE `user_read_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `book_id` bigint(20) NOT NULL COMMENT '小说ID',
  `pre_content_id` bigint(20) DEFAULT NULL COMMENT '上一次阅读的章节内容表ID',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_uq_userid_bookid` (`user_id`,`book_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=119 DEFAULT CHARSET=utf8mb4 COMMENT='用户阅读记录表';

-- ----------------------------
-- Records of user_read_history
-- ----------------------------
INSERT INTO `user_read_history` VALUES ('49', '1255060328322027520', '1254944717113274368', '1254944717314600960', '2020-04-28 17:05:48', '2020-04-28 17:05:48');
INSERT INTO `user_read_history` VALUES ('52', '1255060328322027520', '1254944968184311808', '1254944969023172608', '2020-04-28 17:12:31', '2020-04-28 17:12:31');
INSERT INTO `user_read_history` VALUES ('53', '1255379610071322624', '1254682148440047616', '1254682148729454592', '2020-04-29 14:13:28', '2020-04-29 14:13:28');
INSERT INTO `user_read_history` VALUES ('54', '1255379610071322624', '1254676970567565312', '1254676970794057728', '2020-04-29 14:17:36', '2020-04-29 14:17:36');
INSERT INTO `user_read_history` VALUES ('60', '1255060328322027520', '1254677251162308608', '1254677251367829504', '2020-04-30 07:32:03', '2020-04-30 07:32:03');
INSERT INTO `user_read_history` VALUES ('61', '1255060328322027520', '1254677887534694400', '1254677887790546944', '2020-04-30 07:36:03', '2020-04-30 07:36:03');
INSERT INTO `user_read_history` VALUES ('64', '1255060328322027520', '1254678892443795456', '1254678893156827136', '2020-04-30 08:34:00', '2020-04-30 08:34:00');
INSERT INTO `user_read_history` VALUES ('65', '1255664783722586112', '1254674396451897344', '1254674396690972672', '2020-04-30 09:06:59', '2020-04-30 09:06:59');
INSERT INTO `user_read_history` VALUES ('66', '1255664783722586112', '1254945413401292800', '1254945551112876032', '2020-04-30 09:09:46', '2020-04-30 09:09:46');
INSERT INTO `user_read_history` VALUES ('68', '1255664783722586112', '1254681071191785472', '1254681071552495616', '2020-04-30 09:14:31', '2020-04-30 09:14:31');
INSERT INTO `user_read_history` VALUES ('75', '1255060328322027520', '1254677745226153984', '1254677746505416704', '2020-04-30 09:53:17', '2020-04-30 09:53:17');
INSERT INTO `user_read_history` VALUES ('90', '1255060328322027520', '1254681753466634240', '1254681754687176704', '2020-04-30 10:47:47', '2020-04-30 10:47:47');
INSERT INTO `user_read_history` VALUES ('91', '1255060328322027520', '1254943211274252288', '1254943211462995968', '2020-04-30 10:53:51', '2020-04-30 10:53:51');
INSERT INTO `user_read_history` VALUES ('104', '1255060328322027520', '1254675826696978432', '3263201', '2020-04-30 18:53:02', '2020-04-30 18:53:02');
INSERT INTO `user_read_history` VALUES ('111', '1255060328322027520', '1254957626056912896', '3336649', '2020-04-30 19:11:57', '2020-04-30 19:11:57');
INSERT INTO `user_read_history` VALUES ('113', '1255060328322027520', '1254676970567565312', '3264258', '2020-04-30 19:19:11', '2020-04-30 19:19:11');
INSERT INTO `user_read_history` VALUES ('117', '1255060328322027520', '1254946661743603712', '1254946914001629184', '2020-04-30 19:37:09', '2020-04-30 19:37:09');
INSERT INTO `user_read_history` VALUES ('118', '1255060328322027520', '1254957312633352192', '3335449', '2020-04-30 19:37:36', '2020-04-30 19:37:36');



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
UPDATE `crawl_source` SET `source_name` = '书趣阁', `crawl_rule` = '{\n	\"bookListUrl\": \"http://m.shuquge.com/sort/{catId}/0_{page}.html\",\n	\"catIdRule\": {\n		\"catId1\": \"1\",\n		\"catId2\": \"2\",\n		\"catId3\": \"3\",\n		\"catId4\": \"4\",\n		\"catId5\": \"7\",\n		\"catId6\": \"6\",\n		\"catId7\": \"8\"\n	},\n	\"bookIdPatten\": \"href=\\\"/s/(\\\\d+)\\\\.html\\\"\",\n	\"pagePatten\": \"第(\\\\d+)/\\\\d+页\",\n	\"totalPagePatten\": \"第\\\\d+/(\\\\d+)页\",\n	\"bookDetailUrl\": \"http://m.shuquge.com/s/{bookId}.html\",\n	\"bookNamePatten\": \"<a\\\\s+href=\\\"/s/\\\\d+\\\\.html\\\"><h2>([^/]+)</h2></a>\",\n	\"authorNamePatten\": \"<p>作者：([^/]+)</p>\",\n	\"picUrlPatten\": \"src=\\\"(http://www.shuquge.com/files/article/image/\\\\d+/\\\\d+/\\\\d+s\\\\.jpg)\\\"\",\n	\"statusPatten\": \"<p>状态：([^/]+)</p>\",\n	\"bookStatusRule\": {\n		\"连载中\": 0,\n		\"完本\": 1\n	},\n	\"descStart\": \"<div class=\\\"intro_info\\\">\",\n	\"descEnd\": \"最新章节推荐地址\",\n	\"bookIndexUrl\": \"http://www.shuquge.com/txt/{bookId}/index.html\",\n	\"bookIndexStart\": \"<dt>《\",\n	\"indexIdPatten\": \"<dd><a\\\\s+href=\\\"(\\\\d+)\\\\.html\\\">[^/]+</a></dd>\",\n	\"indexNamePatten\": \"<dd><a\\\\s+href=\\\"\\\\d+\\\\.html\\\">([^/]+)</a></dd>\",\n	\"bookContentUrl\": \"http://www.shuquge.com/txt/{bookId}/{indexId}.html\",\n	\"contentStart\": \"<div id=\\\"content\\\" class=\\\"showtxt\\\">\",\n	\"contentEnd\": \"http://www.shuquge.com\"\n}', `source_status` = 1, `create_time` = '2020-05-18 12:02:34', `update_time` = '2020-05-18 12:02:34' WHERE `id` = 4;

INSERT INTO `friend_link` ( `link_name`, `link_url`, `sort`, `is_open`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES
('小羊影视', 'http://video.java2nb.com/', 11, 1, NULL, NULL, NULL, NULL),
('官方论坛', 'http://bbs.java2nb.com', 21, 1, NULL, NULL, NULL, NULL);


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


alter table book add column `yesterday_buy` int(11) DEFAULT '0' COMMENT '昨日订阅数' after comment_count;

alter table book_index add column `book_price` int(3) DEFAULT 0 COMMENT '章节费用（屋币）' after `is_vip`;