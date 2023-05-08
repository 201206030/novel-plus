INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (305, '301', '修改', null, 'novel:websiteInfo:edit', '2', null, '6');
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 305);