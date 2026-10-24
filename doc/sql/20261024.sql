-- 说明：
--   1. 本次基线全部落在 menu_id >= 1000 区间，与历史数据（< 1000）完全隔离，便于识别。
--   2. 超级管理员无需授权明细：角色标识为 admin 的用户持有全部菜单（代码内 bypass）。
--   3. 内置超级管理员账号 admin，初始密码 admin，记得登录后修改。
--   3. 会清除 menu_id < 1000 的全部旧数据（旧版后台业务菜单 + 历史演示菜单），执行后旧数据会备份到 bak_ 开头的表中。
--   4. 权限只分两级：目录（type=0）与菜单/页面（type=1）。勾中页面即拥有该页面的一切操作，
--      不再建按钮级（type=2）权限；后端也相应只按模块 URL 前缀判定，不做方法级判定。
-- ----------------------------------------------------------------------------

-- 备份老数据。
CREATE TABLE bak_sys_menu_20261023 AS SELECT * FROM sys_menu;

CREATE TABLE bak_sys_role_20261023 AS SELECT * FROM sys_role;

CREATE TABLE bak_sys_user_20261023 AS SELECT * FROM sys_user;

CREATE TABLE bak_sys_role_menu_20261023 AS SELECT * FROM sys_role_menu;

CREATE TABLE bak_sys_user_role_20261023 AS SELECT * FROM sys_user_role;

-- 备份校验：src 与 bak 行数应一致；若两边都是 0，说明连错库或数据已被清理，请中止后面的删除
SELECT 'sys_menu' tbl,
       (SELECT count(*) FROM sys_menu)              src,
       (SELECT count(*) FROM bak_sys_menu_20261023)  bak
FROM DUAL
UNION ALL SELECT 'sys_role',      (SELECT count(*) FROM sys_role),      (SELECT count(*) FROM bak_sys_role_20261023)      FROM DUAL
UNION ALL SELECT 'sys_user',      (SELECT count(*) FROM sys_user),      (SELECT count(*) FROM bak_sys_user_20261023)      FROM DUAL
UNION ALL SELECT 'sys_role_menu', (SELECT count(*) FROM sys_role_menu), (SELECT count(*) FROM bak_sys_role_menu_20261023) FROM DUAL
UNION ALL SELECT 'sys_user_role', (SELECT count(*) FROM sys_user_role), (SELECT count(*) FROM bak_sys_user_role_20261023) FROM DUAL;

-- 回滚方式：
--      DELETE FROM sys_role_menu; INSERT INTO sys_role_menu SELECT * FROM bak_sys_role_menu_20261023;
--      DELETE FROM sys_menu;      INSERT INTO sys_menu      SELECT * FROM bak_sys_menu_20261023;
--      DELETE FROM sys_user_role; INSERT INTO sys_user_role SELECT * FROM bak_sys_user_role_20261023;
--      DELETE FROM sys_user;      INSERT INTO sys_user      SELECT * FROM bak_sys_user_20261023;
--      DELETE FROM sys_role;      INSERT INTO sys_role      SELECT * FROM bak_sys_role_20261023;

-- ---------------- 清理旧版菜单与授权（已备份，可回滚） ----------------
-- 包括旧版后台业务菜单与历史演示菜单（定时任务/数据权限/生成列表等）
DELETE FROM sys_role_menu WHERE menu_id < 1000;
DELETE FROM sys_menu WHERE menu_id < 1000;

-- ---------------- 清理本区间数据（保证可重复执行） ----------------
-- sys_role_menu 没有唯一键，少了这四行会让授权记录每次重跑成倍叠加；
-- 菜单主键固定，少了它重跑会在 INSERT 目录处直接报主键冲突
DELETE FROM sys_role_menu WHERE menu_id >= 1000;
DELETE FROM sys_menu WHERE menu_id >= 1000;
DELETE FROM sys_user_role WHERE user_id IN (1200, 1201);
DELETE FROM sys_user WHERE user_id IN (1200, 1201);

-- ---------------- 一级目录（type=0，perms 为模块可用权限；页面权限即靠它判定） ----------------
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num, gmt_create)
VALUES (1000, 0, '小说管理', '', 'book:use', 0, 'fa fa-book', 1, now()),
       (1010, 0, '会员管理', '', 'member:use', 0, 'fa fa-users', 2, now()),
       (1020, 0, '作家管理', '', 'author:use', 0, 'fa fa-pen', 3, now()),
       (1030, 0, 'AI创作管理', '', 'ai:use', 0, 'fa fa-magic', 4, now()),
       (1040, 0, '内容运营', '', 'news:use', 0, 'fa fa-newspaper-o', 5, now()),
       (1050, 0, '网站设置', '', 'website:use', 0, 'fa fa-cog', 6, now()),
       (1060, 0, '系统管理', '', 'sys:use', 0, 'fa fa-shield', 7, now());

-- ---------------- 二级菜单（type=1，url 必须与侧栏链接一致） ----------------
INSERT INTO sys_menu (menu_id, parent_id, name, url, perms, type, icon, order_num, gmt_create)
VALUES (1001, 1000, '小说列表', '/book/book.html', 'book:use', 1, '', 1, now()),
       (1002, 1000, '小说分类', '/book/category.html', 'book:use', 1, '', 2, now()),
       (1003, 1000, '书单管理', '/bookList/bookList.html', 'bookList:use', 1, '', 3, now()),
       (1004, 1000, '书评管理', '/comment/bookComment.html', 'comment:use', 1, '', 4, now()),
       (1005, 1000, '段评管理', '/comment/paragraph.html', 'comment:use', 1, '', 5, now()),

       (1011, 1010, '会员列表', '/member/user.html', 'member:use', 1, '', 1, now()),
       (1012, 1010, '会员反馈', '/member/userFeedback.html', 'member:use', 1, '', 2, now()),
       (1013, 1010, '购买记录', '/member/buyRecord.html', 'member:use', 1, '', 3, now()),
       (1014, 1010, '消息管理', '/message/userMessage.html', 'message:use', 1, '', 4, now()),
       (1015, 1010, '订单管理', '/order/pay.html', 'order:use', 1, '', 5, now()),

       (1021, 1020, '作者列表', '/author/author.html', 'author:use', 1, '', 1, now()),
       (1022, 1020, '邀请码管理', '/author/authorCode.html', 'author:use', 1, '', 2, now()),
       (1023, 1020, '作家收入', '/author/income.html', 'author:use', 1, '', 3, now()),

       (1031, 1030, 'AI创作任务', '/ai/task.html', 'ai:use', 1, '', 1, now()),
       (1032, 1030, 'AI创作方案', '/ai/plan.html', 'ai:use', 1, '', 2, now()),
       (1033, 1030, 'AI创作偏好', '/ai/pref.html', 'ai:use', 1, '', 3, now()),

       (1041, 1040, '小说推荐', '/website/bookSetting.html', 'website:use', 1, '', 1, now()),
       (1042, 1040, '新闻管理', '/news/news.html', 'news:use', 1, '', 2, now()),
       (1043, 1040, '新闻类别', '/news/category.html', 'news:use', 1, '', 3, now()),

       (1051, 1050, '网站信息', '/website/websiteInfo.html', 'website:use', 1, '', 1, now()),
       (1052, 1050, '友情链接', '/website/friendLink.html', 'website:use', 1, '', 2, now()),

       (1061, 1060, '用户管理', '/sys/user.html', 'sys:use', 1, '', 1, now()),
       (1062, 1060, '角色管理', '/sys/role.html', 'sys:use', 1, '', 2, now()),
       (1063, 1060, '权限管理', '/sys/menu.html', 'sys:use', 1, '', 3, now());

-- ---------------- 清除按钮级权限（只保留目录 + 菜单两级） ----------------
-- 早期基线建过 type=2 的按钮权限（sys:user:create / sys:role:grant 等 11 个），
-- 页面权限已代表该页全部操作，后端也不再引用这些标识；留着它们会让授权树
-- 看起来好像能按按钮粒度控权，实际勾与不勾效果一样，故连同引用它们的授权一并清掉
DELETE FROM sys_role_menu WHERE menu_id IN
    (SELECT menu_id FROM sys_menu WHERE type = 2 AND menu_id >= 1000);
DELETE FROM sys_menu WHERE type = 2 AND menu_id >= 1000;

-- ---------------- 角色示例 ----------------
-- role_id=1（role_sign=admin）通常已存在，即为超级管理员，不改它的名称；
-- 若库里没有，则补一条，保证系统始终有一个超管角色。
INSERT INTO sys_role (role_id, role_name, role_sign, remark, gmt_create, gmt_modified)
SELECT 1, '超级管理员', 'admin', '拥有全部菜单与操作权限', now(), now()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE LOWER(role_sign) = 'admin');

-- 统一超管角色名称为「超级管理员」（上面的 INSERT 只在角色缺失时才新建，
-- 历史库里已有的“超级用户角色”靠这条改名为统一的“超级管理员”）
UPDATE sys_role SET role_name = '超级管理员', remark = '拥有全部菜单与操作权限' WHERE LOWER(role_sign) = 'admin';

INSERT IGNORE INTO sys_role (role_id, role_name, role_sign, remark, gmt_create, gmt_modified)
VALUES (2, '内容编辑', 'editor', '负责小说与内容运营，不含会员/系统管理', now(), now()),
       (3, '会员客服', 'service', '负责会员相关，不含内容与系统管理', now(), now());

-- 内容编辑授权
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 2, menu_id FROM sys_menu
WHERE menu_id IN (1000, 1001, 1002, 1003, 1004, 1005, 1040, 1041, 1042, 1043);

-- 会员客服授权
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu
WHERE menu_id IN (1010, 1011, 1012, 1013, 1014, 1015);

-- ---------------- 账号示例 ----------------
INSERT INTO sys_user (user_id, username, name, password, email, mobile, status, gmt_create, gmt_modified)
VALUES (1200, 'editor', '内容编辑', '21232f297a57a5a743894a0e4a801fc3', 'editor@example.com', '13800000002', 1, now(), now()),
       (1201, 'service', '会员客服', '21232f297a57a5a743894a0e4a801fc3', 'service@example.com', '13800000003', 1, now(), now());

INSERT INTO sys_user_role (user_id, role_id) VALUES (1200, 2), (1201, 3);

-- ---------------- 存量 admin 账号处理 ----------------
-- 历史 sys_user.admin 的口令统一改为 admin 的 MD5，
-- 注意：这会覆盖该账号原有密码，执行后请尽快修改密码。
UPDATE sys_user
SET password = '21232f297a57a5a743894a0e4a801fc3',
    name = '超级管理员',
    status = 1,

    gmt_modified = now()
WHERE username = 'admin';

-- 保证 admin 账号确实关联到 role_sign=admin 的角色
-- （先取变量再先删后插，避开 MySQL 不允许 INSERT...SELECT 子查询引用目标表的限制）
SET @admin_user_id = (SELECT user_id FROM sys_user WHERE username = 'admin' LIMIT 1);
SET @admin_role_id = (SELECT role_id FROM sys_role WHERE LOWER(role_sign) = 'admin' LIMIT 1);

DELETE FROM sys_user_role
WHERE user_id = @admin_user_id AND role_id = @admin_role_id;

INSERT INTO sys_user_role (user_id, role_id)
SELECT @admin_user_id, @admin_role_id
FROM DUAL
WHERE @admin_user_id IS NOT NULL AND @admin_role_id IS NOT NULL;

-- 清理 admin 账号上指向不存在角色的历史关联（保留下面重新计算的正常关联）
DELETE ur FROM sys_user_role ur
    LEFT JOIN sys_role r ON ur.role_id = r.role_id
WHERE r.role_id IS NULL;

-- ---------------- 授权表残留清理 ----------------
-- 引用了已不存在的菜单/角色的关联记录一律删除（多次执行也安全）
DELETE rm FROM sys_role_menu rm
    LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id
WHERE m.menu_id IS NULL;

DELETE rm FROM sys_role_menu rm
    LEFT JOIN sys_role r ON rm.role_id = r.role_id
WHERE r.role_id IS NULL;

-- ---------------- 结果校验（只读，可反复执行） ----------------
SELECT '菜单总数' item, count(*) value FROM sys_menu
UNION ALL SELECT '其中页面菜单(url非空)', count(*) FROM sys_menu WHERE url IS NOT NULL AND url != ''
UNION ALL SELECT '其中按钮权限（应为 0）', count(*) FROM sys_menu WHERE type = 2
UNION ALL SELECT '有效授权记录', count(*) FROM sys_role_menu
UNION ALL SELECT '后台账号', count(*) FROM sys_user WHERE status = 1
UNION ALL SELECT '可登录角色', count(*) FROM sys_role;

-- ---------------- （可选）备份表确认无误后自行清理 ----------------
-- 本脚本维护区间不碰 bak_ 开头的表，所以它们会一直留在库里；确认新基线没问题后可手动执行：
-- DROP TABLE IF EXISTS bak_sys_menu_20261023;
-- DROP TABLE IF EXISTS bak_sys_role_20261023;
-- DROP TABLE IF EXISTS bak_sys_user_20261023;
-- DROP TABLE IF EXISTS bak_sys_role_menu_20261023;
-- DROP TABLE IF EXISTS bak_sys_user_role_20261023;
