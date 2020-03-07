package com.yeqifu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.sys.common.Constast;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.TreeNode;
import com.yeqifu.sys.entity.Permission;
import com.yeqifu.sys.service.IPermissionService;
import com.yeqifu.sys.vo.PermissionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-22
 */
@RestController
@RequestMapping("permission")
public class PermissionController {
    
    @Autowired
    private IPermissionService permissionService;

    /**
     * 加载权限左边的权限树
     * @param permissionVo
     * @return
     */
    @RequestMapping("loadPermissionManagerLeftTreeJson")
    public DataGridView loadPermissionManagerLeftTreeJson(PermissionVo permissionVo){
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        queryWrapper.eq("type", Constast.TYPE_MENU);
        //查询出所有的权限，存放进list中
        List<Permission> list = permissionService.list(queryWrapper);
        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        //将权限放入treeNodes中，组装成json
        for (Permission permission : list) {
            Boolean open = permission.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(permission.getId(),permission.getPid(),permission.getTitle(),open));
        }
        return new DataGridView(treeNodes);
    }

    /**
     * 查询所有权限数据
     * @param permissionVo
     * @return
     */
    @RequestMapping("loadAllPermission")
    public DataGridView loadAllPermission(PermissionVo permissionVo){
        IPage<Permission> page = new Page<>(permissionVo.getPage(),permissionVo.getLimit());
        //进行模糊查询
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        //只能查询权限
        queryWrapper.eq("type",Constast.TYPE_PERMISSION);
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getTitle()),"title",permissionVo.getTitle());
        queryWrapper.like(StringUtils.isNotBlank(permissionVo.getPercode()),"percode",permissionVo.getPercode());
        queryWrapper.eq(permissionVo.getId()!=null,"pid",permissionVo.getId());
        queryWrapper.orderByAsc("ordernum");
        //进行查询
        permissionService.page(page,queryWrapper);
        //返回DataGridView
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("addPermission")
    public ResultObj addPermission(PermissionVo permissionVo){
        try {
            //设置添加类型为 permission
            permissionVo.setType(Constast.TYPE_PERMISSION);
            permissionService.save(permissionVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 加载排序码
     * @return
     */
    @RequestMapping("loadPermissionMaxOrderNum")
    public Map<String,Object> loadPermissionMaxOrderNum(){
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Permission> page = new Page<>(1,1);
        List<Permission> list = permissionService.page(page,queryWrapper).getRecords();
        if (list.size()>0){
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 更新权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("updatePermission")
    public ResultObj updatePermission(PermissionVo permissionVo){
        try {
            permissionService.updateById(permissionVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 检查当前权限是否有子权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("checkPermissionHasChildrenNode")
    public Map<String,Object> checkPermissionHasChildrenNode(PermissionVo permissionVo){
        Map<String,Object> map = new HashMap<String, Object>();
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",permissionVo.getId());
        List<Permission> list = permissionService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /**
     * 删除权限
     * @param permissionVo
     * @return
     */
    @RequestMapping("deletePermission")
    public ResultObj deletePermission(PermissionVo permissionVo){
        try {
            permissionService.removeById(permissionVo.getId());
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
    
    
}

