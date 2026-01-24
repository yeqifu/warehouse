package com.yeqifu.bus.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图表数据点VO类
 * @author system
 * @since 2024-01-24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataVo {
    
    /**
     * 名称/标签
     */
    private String name;
    
    /**
     * 值
     */
    private Double value;
    
    /**
     * 时间（可选）
     */
    private String time;
    
    public ChartDataVo(String name, Double value) {
        this.name = name;
        this.value = value;
    }
    
}