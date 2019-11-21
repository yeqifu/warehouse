package com.yeqifu.sys.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yeqifu.sys.common.ActiverUser;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.service.IUserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: 落亦-
 * @Date: 2019/11/21 20:44
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loginname",authenticationToken.getPrincipal().toString());
        User user = userService.getOne(queryWrapper);
        if (null!=user){
            ActiverUser activerUser = new ActiverUser();
            activerUser.setUser(user);
            ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPwd(),credentialsSalt,this.getName());
            return info;
        }
        return null;
    }
}
