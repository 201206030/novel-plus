INSERT INTO sys_dict (name, value, type, description, sort, parent_id, create_by, create_date, update_by,
                      update_date, remarks, del_flag)
VALUES ('轮播图', '0', 'book_rec_type', '小说推荐类型', 0, null, null, null, null, null, '', null);
INSERT INTO sys_dict (name, value, type, description, sort, parent_id, create_by, create_date, update_by,
                      update_date, remarks, del_flag)
VALUES ('顶部小说栏', '1', 'book_rec_type', '小说推荐类型', 1, null, null, null, null, null, '', null);
INSERT INTO sys_dict (name, value, type, description, sort, parent_id, create_by, create_date, update_by,
                      update_date, remarks, del_flag)
VALUES ('本周强推', '2', 'book_rec_type', '小说推荐类型', 2, null, null, null, null, null, '', null);
INSERT INTO sys_dict (name, value, type, description, sort, parent_id, create_by, create_date, update_by,
                      update_date, remarks, del_flag)
VALUES ('热门推荐', '3', 'book_rec_type', '小说推荐类型', 3, null, null, null, null, null, '', null);
INSERT INTO sys_dict (name, value, type, description, sort, parent_id, create_by, create_date, update_by,
                      update_date, remarks, del_flag)
VALUES ('精品推荐', '4', 'book_rec_type', '小说推荐类型', 4, null, null, null, null, null, '', null);



INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (320, '300', '小说推荐', 'novel/bookSetting', 'novel:bookSetting:bookSetting', '1', 'fa', '6');

INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (321, '320', '查看', null, 'novel:bookSetting:detail', '2', null, '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (322, '320', '新增', null, 'novel:bookSetting:add', '2', null, '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (323, '320', '修改', null, 'novel:bookSetting:edit', '2', null, '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (324, '320', '删除', null, 'novel:bookSetting:remove', '2', null, '6');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (325, '320', '批量删除', null, 'novel:bookSetting:batchRemove', '2', null, '6');

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 320);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 321);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 322);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 323);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 324);
INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 325);


INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES (410, '400', '会员反馈', 'novel/userFeedback', 'novel:userFeedback:userFeedback', '1', 'fa', '16');

INSERT INTO sys_role_menu (role_id, menu_id)
VALUES (1, 410);
