-- 添加可视化菜单
INSERT INTO sys_permission (pid, type, title, percode, icon, href, target, open, ordernum, available) 
VALUES (1, 'menu', '数据可视化', NULL, '&#xe63c;', '/visualization/toVisualizationManager', '_self', 0, 100, 1);

-- 为所有角色添加可视化菜单权限
INSERT INTO sys_role_permission (rid, pid) 
SELECT rid, (SELECT id FROM sys_permission WHERE title = '数据可视化') 
FROM sys_role;
