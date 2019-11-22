package com.yeqifu.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeqifu.sys.common.*;
import com.yeqifu.sys.entity.Permission;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.service.IPermissionService;
import com.yeqifu.sys.vo.PermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 落亦-
 * @Date: 2019/11/22 15:35
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IPermissionService permissionService;

    @RequestMapping("loadIndexLeftMenuJson")
    public DataGridView loadIndexLeftMenuJson(PermissionVo permissionVo){
        //查询所有菜单
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>();
        //设置查询条件
        //查询的必须是菜单，不能是crud的权限
        queryWrapper.eq("type",Constast.TYPE_MENU);
        //菜单必须可用
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);

        //获得用户  判断用户的类型
        User user = (User) WebUtils.getSession().getAttribute("user");
        List<Permission> list = null;
        if (user.getType().equals(Constast.USER_TYPE_SUPER)){
            //用户类型为超级管理员
            list = permissionService.list(queryWrapper);
        }else {
            //用户类型为 管理员和普通用户
            list = permissionService.list(queryWrapper);
        }

        List<TreeNode> treeNodes = new ArrayList<TreeNode>();
        for (Permission p : list) {
            Integer id =p.getId();
            Integer pid = p.getPid();
            String title = p.getTitle();
            String icon = p.getIcon();
            String href = p.getHref();
            Boolean spread = p.getOpen().equals(Constast.OPEN_TRUE)?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,spread));
        }

        //构造层级关系
        List<TreeNode> list2 = TreeNodeBuilder.build(treeNodes,1);
        return new DataGridView(list2);

    }

}
