package com.yeqifu.sys.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.sys.common.Constast;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.entity.Dept;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.service.IDeptService;
import com.yeqifu.sys.service.IUserService;
import com.yeqifu.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`deptid`) REFER `warehouse/sys_dept`(`id`) ON UPDATE CASC 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-21
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    /**
     * 查询所有用户
     * @param userVo
     * @return
     */
    @RequestMapping("loadAllUser")
    public DataGridView loadAllUser(UserVo userVo){
        IPage<User> page = new Page<User>(userVo.getPage(),userVo.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getName()),"loginname",userVo.getName()).or().eq(StringUtils.isNotBlank(userVo.getName()),"name",userVo.getName());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getAddress()),"address",userVo.getAddress());
        //查询系统用户
        queryWrapper.eq("type", Constast.USER_TYPE_NORMAL);
        queryWrapper.eq(userVo.getDeptid()!=null,"deptid",userVo.getDeptid());
        userService.page(page,queryWrapper);

        //将所有用户数据放入list中
        List<User> list = page.getRecords();
        for (User user : list) {
            Integer deptid = user.getDeptid();
            if (deptid!=null){
                //先从缓存中去取，如果缓存中没有就去数据库中取
                Dept one = deptService.getById(deptid);
                //设置user的部门名称
                user.setDeptname(one.getName());
            }
            Integer mgr = user.getMgr();
            if (mgr!=null){
                User one = userService.getById(mgr);
                //设置user的领导名称
                user.setLeadername(one.getName());
            }
        }
        return new DataGridView(page.getTotal(),list);
    }
}

