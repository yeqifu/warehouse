package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.Sales;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-21
 */
public interface ISalesService extends IService<Sales> {

    /**
     * 获取销售总额
     * @return
     */
    Double getTotalSales();

    /**
     * 获取销售商品数量
     * @return
     */
    Integer getSalesCount();

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
}
