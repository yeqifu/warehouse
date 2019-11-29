package com.yeqifu.sys.mapper;

import com.yeqifu.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 9216 kB Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-11-28
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色ID删除sys_role_permission表中的数据
     * @param id 角色的id
     */
    void deleteRolePermissionByRid(@Param("pid") Serializable id);

    /**
     * 根据角色ID删除sys_user_role表中的数据
     * @param id 角色的id
     */
    void deleteUserRoleByRid(@Param("pid") Serializable id);
}
