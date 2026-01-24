-- 数据可视化菜单添加脚本
-- 请在数据库中执行以下SQL语句

-- 首先检查是否存在父级菜单，如果没有则创建父级菜单
INSERT INTO sys_permission (id, pid, type, title, percode, icon, href, target, open, ordernum, available) 
VALUES 
(1000, 1, 'menu', '数据分析', NULL, '&#xe629;', '', NULL, 1, 100, 1);

-- 添加数据可视化子菜单
INSERT INTO sys_permission (id, pid, type, title, percode, icon, href, target, open, ordernum, available) 
VALUES 
(1001, 1000, 'menu', '数据可视化', NULL, '&#xe629;', '/resources/page/chart/chartAnalysis.html', NULL, 0, 101, 1);

-- 为超级管理员角色分配数据可视化菜单权限（角色ID为1通常是超级管理员）
INSERT INTO sys_role_permission (rid, pid) VALUES (1, 1000);
INSERT INTO sys_role_permission (rid, pid) VALUES (1, 1001);

-- 如果存在其他管理员角色，也请为他们分配权限
-- INSERT INTO sys_role_permission (rid, pid) VALUES (2, 1000);
-- INSERT INTO sys_role_permission (rid, pid) VALUES (2, 1001);