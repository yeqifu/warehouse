package com.yeqifu.sys.controller;

import com.yeqifu.sys.common.ActiverUser;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登陆前端控制器
 * @Author: 落亦-
 * @Date: 2019/11/21 21:33
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @RequestMapping("login")
    public ResultObj login(String loginname, String pwd){

        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken token = new UsernamePasswordToken(loginname,pwd);
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user",activerUser.getUser());

            return ResultObj.LOGIN_SUCCESS;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResultObj.LOGIN_ERROR_PASS;
        }

    }

}
