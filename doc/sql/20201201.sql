INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (246, 241, '批量删除', NULL, 'novel:news:batchRemove', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (245, 241, '删除', NULL, 'novel:news:remove', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (244, 241, '修改', NULL, 'novel:news:edit', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (243, 241, '新增', NULL, 'novel:news:add', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (242, 241, '查看', NULL, 'novel:news:detail', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (241, 234, '新闻列表', 'novel/news', 'novel:news:news', 1, 'fa', 8, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (240, 235, '批量删除', NULL, 'novel:category:batchRemove', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (239, 235, '删除', NULL, 'novel:category:remove', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (238, 235, '修改', NULL, 'novel:category:edit', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (237, 235, '新增', NULL, 'novel:category:add', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (236, 235, '查看', NULL, 'novel:category:detail', 2, NULL, 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (235, 234, '类别管理', 'novel/category', 'novel:category:category', 1, 'fa', 6, NULL, NULL);
INSERT INTO `sys_menu`(`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`, `gmt_create`, `gmt_modified`) VALUES (234, 0, '新闻管理', '', '', 0, 'fa fa-newspaper-o', 8, NULL, NULL);



INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4889, 1, 246);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4890, 1, 245);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4891, 1, 244);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4892, 1, 243);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4893, 1, 242);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4899, 1, 241);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4894, 1, 240);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4895, 1, 239);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4896, 1, 238);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4897, 1, 237);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4898, 1, 236);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4900, 1, 235);
INSERT INTO `sys_role_menu`(`id`, `role_id`, `menu_id`) VALUES (4888, 1, 234);


delete from sys_menu where menu_id = 202;