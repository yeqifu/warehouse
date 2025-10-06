package com.yeqifu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.sys.common.Constast;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.TreeNode;
import com.yeqifu.sys.entity.Permission;
import com.yeqifu.sys.entity.Role;
import com.yeqifu.sys.service.IPermissionService;
import com.yeqifu.sys.service.IRoleService;
import com.yeqifu.sys.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    /**
     * 查询所有角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping("loadAllRole")
    public DataGridView loadAllRole(RoleVo roleVo) {
        IPage<Role> page = new Page<Role>(roleVo.getPage(), roleVo.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()), "name", roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()), "remark", roleVo.getRemark());
        queryWrapper.eq(roleVo.getAvailable() != null, "available", roleVo.getAvailable());
        queryWrapper.ge(roleVo.getStartTime() != null, "createtime", roleVo.getStartTime());
        queryWrapper.le(roleVo.getEndTime() != null, "createtime", roleVo.getEndTime());
        queryWrapper.orderByDesc("createtime");
        roleService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping("addRole")
    public ResultObj addRole(RoleVo roleVo) {
        try {
            roleVo.setCreatetime(new Date());
            roleService.save(roleVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改角色
     *
     * @param roleVo
     * @return
     */
    @RequestMapping("updateRole")
    public ResultObj updateRole(RoleVo roleVo) {
        try {
            roleService.updateById(roleVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("deleteRole")
    public ResultObj deleteRole(Integer id) {
        try {
            this.roleService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据角色ID加载菜单和权限的树的json串
     *
     * @param roleId
     * @return
     */
    @RequestMapping("initPermissionByRoleId")
    public DataGridView initPermissionByRoleId(Integer roleId) {
        //查询所有可用的菜单和权限
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Permission> allPermissions = permissionService.list(queryWrapper);
        //1.首先根据角色id查询出当前角色所拥有的所有菜单的ID和权限的ID
        List<Integer> currentRolePermissions = roleService.queryRolePermissionIdsByRid(roleId);
        //2.根据查询出来的菜单ID和权限ID，再查询出菜单的数据和权限的数据
        List<Permission> currentPermissions = null;
        //如果根据角色id查询出来了菜单ID或权限ID，就去查询
        if (currentRolePermissions.size() > 0) {
            queryWrapper.in("id", currentRolePermissions);
            currentPermissions = permissionService.list(queryWrapper);
        } else {
            currentPermissions = new ArrayList<>();
        }
        //构造List<TreeNode>
        List<TreeNode> nodes = new ArrayList<>();
        for (Permission allPermission : allPermissions) {
            String checkArr = "0";
            for (Permission currentPermission : currentPermissions) {
                if (allPermission.getId().equals(currentPermission.getId())) {
                    checkArr = "1";
                    break;
                }
            }
            Boolean spread = (allPermission.getOpen() == null || allPermission.getOpen() == 1) ? true : false;
            nodes.add(new TreeNode(allPermission.getId(), allPermission.getPid(), allPermission.getTitle(), spread, checkArr));
        }
        return new DataGridView(nodes);
    }

    /**
     * 保存角色和菜单权限之间的关系
     *
     * @param rid
     * @param ids
     * @return
     */
    @RequestMapping("saveRolePermission")
    public ResultObj saveRolePermission(Integer rid, Integer[] ids) {
        try {
            roleService.saveRolePermission(rid, ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }

    }

}

