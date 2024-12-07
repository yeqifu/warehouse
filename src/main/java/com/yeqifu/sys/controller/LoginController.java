package com.yeqifu.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.yeqifu.sys.common.ActiverUser;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.Loginfo;
import com.yeqifu.sys.service.ILoginfoService;
import com.yeqifu.sys.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * 登陆控制
 * @Date: 2019/11/21 21:33
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private ILoginfoService loginfoService;

    @RequestMapping("login")
    public ResultObj login(UserVo userVo,String code,HttpSession session){

        // 从session中获取存储的验证码
        String sessionCode = (String) session.getAttribute("code");

        // 验证验证码
        if (code != null && sessionCode != null && sessionCode.equals(code)) {
            // 创建Shiro的认证令牌
            AuthenticationToken token = new UsernamePasswordToken(userVo.getLoginname(), userVo.getPwd());
            Subject subject = SecurityUtils.getSubject();

            try {
                // 执行登录操作
                subject.login(token);

                // 获取已认证的用户信息
                ActiverUser activerUser = (ActiverUser) subject.getPrincipal();

                // 将用户信息存储到session中
                session.setAttribute("user", activerUser.getUser());

                // 记录登录日志
                Loginfo entity = new Loginfo();
                entity.setLoginname(activerUser.getUser().getName() + "-" + activerUser.getUser().getLoginname());
                entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
                entity.setLogintime(new Date());
                loginfoService.save(entity);

                // 返回登录成功的结果
                return ResultObj.LOGIN_SUCCESS;
            } catch (AuthenticationException e) {
                // 处理认证异常，返回登录失败的结果
                e.printStackTrace();
                return ResultObj.LOGIN_ERROR_PASS;
            }
        } else {
            // 验证码错误，返回验证码错误的结果
            return ResultObj.LOGIN_ERROR_CODE;
        }

    }

    /**
     *
     * @param response
     * @param session
     * @throws IOException
     */
    @RequestMapping("getCode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException{
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setDateHeader("Expires", 0);

        // 创建图形验证码（这里假设CaptchaUtil和LineCaptcha是已经存在的工具类）
        // 您可以根据需要调整验证码的参数，如长度、宽度、线条数、字符数等
        BufferedImage captchaImage = CaptchaUtil.createLineCaptcha(116, 36, 4, 5);
        String captchaCode = CaptchaUtil.createLineCaptcha(captchaImage); // 假设有一个方法来从图像中检索验证码文本

        // 将验证码文本存储在session中
        session.setAttribute("code", captchaCode);

        // 将图形验证码写入响应输出流
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(captchaImage, "jpeg", outputStream);
            outputStream.flush(); // 确保数据被发送出去
        } catch (IOException e) {
            // 处理IO异常，这里只是简单地打印堆栈跟踪，但在实际应用中可能需要更复杂的错误处理
            e.printStackTrace();
            throw e; // 重新抛出异常，以便Spring可以处理它（例如，通过错误页面）
        }
    }

}
