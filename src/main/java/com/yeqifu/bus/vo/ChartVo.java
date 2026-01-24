package com.yeqifu.bus.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 图表数据VO类
 * @author system
 * @since 2024-01-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ChartVo {
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 统计类型：day,month,year
     */
    private String type;
    
    /**
     * 商品ID
     */
    private Integer goodsid;
    
    /**
     * 客户ID
     */
    private Integer customerid;
    
}