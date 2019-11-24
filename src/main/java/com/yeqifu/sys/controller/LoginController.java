package com.yeqifu.sys.controller;

import com.yeqifu.sys.common.ActiverUser;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.Loginfo;
import com.yeqifu.sys.service.ILoginfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 登陆前端控制器
 * @Author: 落亦-
 * @Date: 2019/11/21 21:33
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private ILoginfoService loginfoService;

    @RequestMapping("login")
    public ResultObj login(String loginname, String pwd){

        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(loginname,pwd);
        try {
            //对用户进行认证登陆
            subject.login(token);
            //通过subject获取以认证活动的user
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            //将user存储到session中
            WebUtils.getSession().setAttribute("user",activerUser.getUser());

            //记录登陆日志
            Loginfo entity = new Loginfo();
            entity.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
            entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
            entity.setLogintime(new Date());
            loginfoService.save(entity);
            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }

    }

}
