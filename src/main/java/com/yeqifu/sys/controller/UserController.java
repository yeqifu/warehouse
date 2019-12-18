package com.yeqifu.sys.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.sys.common.Constast;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.PinyinUtils;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.entity.Dept;
import com.yeqifu.sys.entity.Role;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.service.IDeptService;
import com.yeqifu.sys.service.IRoleService;
import com.yeqifu.sys.service.IUserService;
import com.yeqifu.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB; (`deptid`) REFER `warehouse/sys_dept`(`id`) ON UPDATE CASC 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-21
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IDeptService deptService;

    @Autowired
    private IRoleService roleService;

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

    /**
     * 加载排序码
     * @return
     */
    @RequestMapping("loadUserMaxOrderNum")
    public Map<String,Object> loadUserMaxOrderNum(){
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<User> page = new Page<>(1,1);
        List<User> list = userService.page(page,queryWrapper).getRecords();
        if (list.size()>0){
            map.put("value",list.get(0).getOrdernum()+1);
        }else {
            map.put("value",1);
        }
        return map;
    }

    /**
     * 根据部门ID查询用户
     * @param deptid
     * @return
     */
    @RequestMapping("loadUsersByDeptId")
    public DataGridView loadUsersByDeptIp(Integer deptid){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(deptid!=null,"deptid",deptid);
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
        queryWrapper.eq("type",Constast.USER_TYPE_NORMAL);
        List<User> list = userService.list(queryWrapper);
        return new DataGridView(list);
    }

    /**
     * 把用户名转成拼音
     * @param username
     * @return
     */
    @RequestMapping("changeChineseToPinyin")
    public Map<String,Object> changeChineseToPinyin(String username){
        Map<String,Object> map = new HashMap<>();
        if (null!=username){
            map.put("value", PinyinUtils.getPingYin(username));
        }else {
            map.put("value","");
        }
        return map;
    }

    /**
     * 添加用户
     * @param userVo
     * @return
     */
    @RequestMapping("addUser")
    public ResultObj addUser(UserVo userVo){
        try {
            //设置类型
            userVo.setType(Constast.USER_TYPE_NORMAL);
            //设置盐
            String salt = IdUtil.simpleUUID().toUpperCase();
            userVo.setSalt(salt);
            //设置密码
            userVo.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());
            userService.save(userVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 根据id查询一个用户
     * @param id  领导的id
     * @return
     */
    @RequestMapping("loadUserById")
    public DataGridView loadUserById(Integer id){
        return new DataGridView(userService.getById(id));
    }

    /**
     * 修改用户
     * @param userVo
     * @return
     */
    @RequestMapping("updateUser")
    public ResultObj updateUser(UserVo userVo){
        try {
            userService.updateById(userVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping("deleteUser/{id}")
    public ResultObj deleteUser(@PathVariable("id") Integer id){
        try {
            userService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    @RequestMapping("resetPwd/{id}")
    public ResultObj resetPwd(@PathVariable("id") Integer id){
        try {
            User user = new User();
            user.setId(id);
            //设置盐  32位(大写英文字母(A-Z)加数字(0-9))
            String salt = IdUtil.simpleUUID().toUpperCase();
            user.setSalt(salt);
            //设置密码
            user.setPwd(new Md5Hash(Constast.USER_DEFAULT_PWD,salt,2).toString());
            userService.updateById(user);
            return ResultObj.RESET_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.RESET_ERROR;
        }
    }

    /**
     * 根据用户id查询角色并选中已拥有的角色
     * @param id 用户id
     * @return
     */
    @RequestMapping("initRoleByUserId")
    public DataGridView initRoleByUserId(Integer id){
        //1.查询所有可用的角色
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("available",Constast.AVAILABLE_TRUE);
        List<Map<String, Object>> listMaps = roleService.listMaps(queryWrapper);
        //2.查询当前用户拥有的角色ID集合
        List<Integer> currentUserRoleIds = roleService.queryUserRoleIdsByUid(id);
        for (Map<String, Object> map : listMaps) {
            Boolean LAY_CHECKED=false;
            Integer roleId = (Integer) map.get("id");
            for (Integer rid : currentUserRoleIds) {
                //如果当前用户已有该角色，则让LAY_CHECKED为true。LAY_CHECKED为true时，复选框选中
                if (rid.equals(roleId)){
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new DataGridView(Long.valueOf(listMaps.size()),listMaps);
    }

    /**
     * 保存用户和角色的关系
     * @param uid 用户的ID
     * @param ids 用户拥有的角色的ID的数组
     * @return
     */
    @RequestMapping("saveUserRole")
    public ResultObj saveUserRole(Integer uid,Integer[] ids){
        try {
            userService.saveUserRole(uid,ids);
            return ResultObj.DISPATCH_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DISPATCH_ERROR;
        }
    }


}

