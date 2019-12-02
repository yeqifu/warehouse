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
import org.springframework.context.annotation.Lazy;

/**
 * @Author: 落亦-
 * @Date: 2019/11/21 20:44
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    /**
     * 当需要使用的时候，才加载。  即：当CacheAspect被解析之后，userService才会解析，要不然切面会不生效
     */
    @Lazy
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
            //生成盐
            ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activerUser,user.getPwd(),credentialsSalt,this.getName());
            return info;
        }
        return null;
    }
}
