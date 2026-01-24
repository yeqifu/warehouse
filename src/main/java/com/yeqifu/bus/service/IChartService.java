package com.yeqifu.bus.service;

import com.yeqifu.bus.vo.ChartDataVo;
import com.yeqifu.bus.vo.ChartVo;

import java.util.List;
import java.util.Map;

/**
 * 图表统计服务接口
 * @author system
 * @since 2024-01-24
 */
public interface IChartService {

    /**
     * 获取销售统计数据
     * @param chartVo 查询条件
     * @return 销售统计数据
     */
    Map<String, Object> getSalesStatistics(ChartVo chartVo);

    /**
     * 获取退货统计数据
     * @param chartVo 查询条件
     * @return 退货统计数据
     */
    Map<String, Object> getSalesbackStatistics(ChartVo chartVo);

    /**
     * 获取商品库存统计
     * @param chartVo 查询条件
     * @return 库存统计数据
     */
    Map<String, Object> getGoodsStockStatistics(ChartVo chartVo);

    /**
     * 获取客户购买统计
     * @param chartVo 查询条件
     * @return 客户统计数据
     */
    Map<String, Object> getCustomerStatistics(ChartVo chartVo);

    /**
     * 获取综合仪表板数据
     * @return 仪表板数据
     */
    Map<String, Object> getDashboardData();
    
}