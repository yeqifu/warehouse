package com.yeqifu.sys.init;

import com.yeqifu.sys.entity.Permission;
import com.yeqifu.sys.service.IPermissionService;
import com.yeqifu.sys.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 菜单初始化类
 * @author luoyi-
 * @since 2026-01-24
 */
@Component
public class MenuInit implements CommandLineRunner {

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IRoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已存在可视化菜单
        Permission visualizationMenu = permissionService.getOne(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Permission>()
                        .eq("title", "数据可视化")
        );

        if (visualizationMenu == null) {
            // 添加可视化菜单
            visualizationMenu = new Permission();
            visualizationMenu.setPid(1);
            visualizationMenu.setType("menu");
            visualizationMenu.setTitle("数据可视化");
            visualizationMenu.setIcon("&#xe63c;");
            visualizationMenu.setHref("/visualization/toVisualizationManager");
            visualizationMenu.setTarget("_self");
            visualizationMenu.setOpen(0);
            visualizationMenu.setOrdernum(100);
            visualizationMenu.setAvailable(1);
            permissionService.save(visualizationMenu);

            // 为所有角色添加可视化菜单权限
            List<Integer> roleIds = roleService.list().stream().map(role -> role.getId()).collect(java.util.stream.Collectors.toList());
            for (Integer roleId : roleIds) {
                // 查询角色已有的权限
                List<Integer> existingPermissions = roleService.queryRolePermissionIdsByRid(roleId);
                existingPermissions.add(visualizationMenu.getId());
                Integer[] permissions = existingPermissions.toArray(new Integer[0]);
                roleService.saveRolePermission(roleId, permissions);
            }

            System.out.println("可视化菜单添加成功");
        } else {
            System.out.println("可视化菜单已存在");
        }
    }
}