package com.yeqifu.bus.service;

import java.util.List;
import java.util.Map;

/**
 * 数据可视化服务接口
 * @author luoyi-
 * @since 2026-01-24
 */
public interface IVisualizationService {

    /**
     * 获取销售总额
     * @return
     */
    Double getTotalSales();

    /**
     * 获取退货总额
     * @return
     */
    Double getTotalSalesback();

    /**
     * 获取销售商品数量
     * @return
     */
    Integer getSalesCount();

    /**
     * 获取退货商品数量
     * @return
     */
    Integer getSalesbackCount();

    /**
     * 获取按月份统计的销售数据
     * @return
     */
    List<Map<String, Object>> getSalesByMonth();

    /**
     * 获取按商品分类统计的销售数据
     * @return
     */
    List<Map<String, Object>> getSalesByCategory();

    /**
     * 获取按客户统计的销售数据
     * @return
     */
    List<Map<String, Object>> getSalesByCustomer();

    /**
     * 获取按月份统计的退货数据
     * @return
     */
    List<Map<String, Object>> getSalesbackByMonth();
}