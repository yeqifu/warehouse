package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.Inport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 落亦-
 * @Date: 2019/12/18 10:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InportVo extends Inport {

    private Integer page = 1;

    private Integer limit = 10;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}
