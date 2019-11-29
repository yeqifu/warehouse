package com.yeqifu.sys.service.impl;

import com.yeqifu.sys.entity.Role;
import com.yeqifu.sys.mapper.RoleMapper;
import com.yeqifu.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public boolean removeById(Serializable id) {
        //根据角色ID删除sys_role_permission表中的数据
        this.getBaseMapper().deleteRolePermissionByRid(id);
        //根据角色ID删除sys_user_role表中的数据
        this.getBaseMapper().deleteUserRoleByRid(id);
        return super.removeById(id);
    }
}
