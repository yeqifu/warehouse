package com.yeqifu.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.mapper.RoleMapper;
import com.yeqifu.sys.mapper.UserMapper;
import com.yeqifu.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB; (`deptid`) REFER `warehouse/sys_dept`(`id`) ON UPDATE CASC 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-21
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean save(User entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeById(Serializable id) {
        //根据用户id删除用户角色中间表的数据
        roleMapper.deleteRoleUserByUid(id);
        //删除用户头像[如果是默认头像不删除，否则删除]

        return super.removeById(id);
    }

    /**
     * 保存用户和角色的关系
     * @param uid 用户的ID
     * @param ids 用户拥有的角色的ID的数组
     */
    @Override
    public void saveUserRole(Integer uid, Integer[] ids) {
        //1.根据用户ID删除sys_user_role里面的数据
        roleMapper.deleteRoleUserByUid(uid);
        if (null!=ids&&ids.length>0){
            for (Integer rid : ids) {
                roleMapper.insertUserRole(uid,rid);
            }
        }
    }

    /**
     * 查询当前用户是否是其他用户的直属领导
     * @param userId        当前用户ID
     * @return              true:是  false:否
     */
    @Override
    public Boolean queryMgrByUserId(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("mgr",userId);
        List<User> users = userMapper.selectList(queryWrapper);
        if (null!=users&&users.size()>0){
            return true;
        }else {
            return false;
        }
    }
}
