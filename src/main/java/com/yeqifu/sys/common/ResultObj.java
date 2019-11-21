package com.yeqifu.sys.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 落亦-
 * @Date: 2019/11/21 21:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultObj {

    public static final ResultObj LOGIN_SUCCESS=new ResultObj(Constast.OK,"登陆成功");
    public static final ResultObj LOGIN_ERROR_PASS=new ResultObj(Constast.ERROR,"用户名或密码错误");
    public static final ResultObj LOGIN_ERROR_CODE=new ResultObj(Constast.ERROR,"验证码错误");

    private Integer code;
    private String msg;

}
