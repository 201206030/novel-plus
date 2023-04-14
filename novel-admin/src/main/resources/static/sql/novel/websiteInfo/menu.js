--菜单SQL
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
VALUES('247', '网站信息表', 'novel/websiteInfo', 'novel:websiteInfo:websiteInfo', '1', 'fa', '6');

--按钮父菜单ID
set
@parentId
= @@identity;

--菜单对应按钮SQL
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
SELECT
@parentId,
'查看', null, 'novel:websiteInfo:detail', '2', null, '6';
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
SELECT
@parentId,
'新增', null, 'novel:websiteInfo:add', '2', null, '6';
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
SELECT
@parentId,
'修改', null, 'novel:websiteInfo:edit', '2', null, '6';
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
SELECT
@parentId,
'删除', null, 'novel:websiteInfo:remove', '2', null, '6';
INSERT
INTO`sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`)
SELECT
@parentId,
'批量删除', null, 'novel:websiteInfo:batchRemove', '2', null, '6';
