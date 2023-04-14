CREATE TABLE `website_info`
(
    id             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    name           varchar(50)  NOT NULL COMMENT '网站名',
    domain         varchar(50)  NOT NULL COMMENT '网站域名',
    keyword        varchar(50)  NOT NULL COMMENT 'SEO关键词',
    description    varchar(512) NOT NULL COMMENT '网站描述',
    qq             varchar(20)  NOT NULL COMMENT '站长QQ',
    logo           varchar(200) NOT NULL COMMENT '网站logo图片（默认）',
    logo_dark      varchar(200) NOT NULL COMMENT '网站logo图片（深色）',
    create_time    datetime null comment '创建时间',
    create_user_id bigint null comment '创建人ID',
    update_time    datetime null comment '更新时间',
    update_user_id bigint null comment '更新人ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='网站信息表';

INSERT INTO website_info (id, name, domain, keyword, description, qq, logo, logo_dark, create_time, create_user_id,
                          update_time, update_user_id)
VALUES (1, '小说精品屋', 'www.xxyopen.com', '小说精品屋,小说,小说CMS,原创文学系统,开源小说系统,免费小说建站程序',
        '小说精品屋是一个多端（PC、WAP）阅读、功能完善的原创文学CMS系统，由前台门户系统、作家后台管理系统、平台后台管理系统、爬虫管理系统等多个子系统构成，支持会员充值、订阅模式、新闻发布和实时统计报表等功能，新书自动入库，老书自动更新。',
        '1179705413', 'https://youdoc.gitee.io/resource/images/logo/logo.png',
        'https://youdoc.gitee.io/resource/images/logo/logo_white.png', null, null, null, null);

INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num, gmt_create, gmt_modified)
VALUES (300, 0, '网站管理', '', '', 0, 'fa fa-television', 6, null, null);

INSERT
INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (301, 300, '网站信息', 'novel/websiteInfo', 'novel:websiteInfo:websiteInfo', '1', 'fa', '6');


INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 300);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 301);


INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (310, 300, '友情链接', 'novel/friendLink', 'novel:friendLink:friendLink', '1', 'fa', '16');

INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (311, 310, '查看', null, 'novel:friendLink:detail', '2', null, '6');
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (312, 310, '新增', null, 'novel:friendLink:add', '2', null, '6');
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (313, 310, '修改', null, 'novel:friendLink:edit', '2', null, '6');
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (314, 310, '删除', null, 'novel:friendLink:remove', '2', null, '6');
INSERT INTO `sys_menu` (menu_id, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (315, 310, '批量删除', null, 'novel:friendLink:batchRemove', '2', null, '6');


INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 310);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 311);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 312);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 313);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 314);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 315);
