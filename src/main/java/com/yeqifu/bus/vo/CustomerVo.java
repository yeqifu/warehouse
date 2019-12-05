package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: 落亦-
 * @Date: 2019/12/5 9:30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerVo extends Customer{

    /**
     * 分页参数，当前是第一页，每页10条数据
     */
    private Integer page=1;
    private Integer limit=10;

    /**
     * 批量删除客户，存放客户ID的数组
     */
    private Integer[] ids;

}
