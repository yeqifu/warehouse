package com.yeqifu.sys.mapper;

import com.yeqifu.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 根据角色ID查询当前角色拥有的菜单ID和权限ID
     * @param roleId
     * @return
     */
    List<Integer> queryRolePermissionIdsByRid(@Param("roleId") Integer roleId);

    /**
     * 保存角色和菜单权限之间的关系
     * @param rid
     * @param pid
     */
    void saveRolePermission(@Param("rid") Integer rid,@Param("pid") Integer pid);

    /**
     * 根据用户id删除用户角色中间表的数据
     * @param id
     */
    void deleteRoleUserByUid(@Param("id") Serializable id);

    /**
     * 查询当前用户拥有的角色ID集合
     * @param id
     * @return
     */
    List<Integer> queryUserRoleIdsByUid(@Param("id") Integer id);

    /**
     * 保存用户和角色的关系
     * @param uid 用户的ID
     * @param rid 用户拥有的角色的ID的数组
     */
    void insertUserRole(@Param("uid") Integer uid,@Param("rid") Integer rid);
}
