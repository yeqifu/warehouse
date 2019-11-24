package com.yeqifu.sys.vo;

import com.yeqifu.sys.entity.Loginfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 落亦-
 * @Date: 2019/11/23 18:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginfoVo extends Loginfo {

    private Integer page;
    private Integer limit;

    //接收多个id
    private Integer[] ids;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
