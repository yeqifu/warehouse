package com.yeqifu.bus.controller;

import com.yeqifu.bus.service.IChartService;
import com.yeqifu.bus.vo.ChartVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 图表统计控制器
 * @author system
 * @since 2024-01-24
 */
@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private IChartService chartService;

    /**
     * 获取销售统计数据
     * @param chartVo 查询条件
     * @return 销售统计数据
     */
    @RequestMapping("/getSalesStatistics")
    public DataGridView getSalesStatistics(ChartVo chartVo) {
        Map<String, Object> data = chartService.getSalesStatistics(chartVo);
        return new DataGridView(data);
    }

    /**
     * 获取退货统计数据
     * @param chartVo 查询条件
     * @return 退货统计数据
     */
    @RequestMapping("/getSalesbackStatistics")
    public DataGridView getSalesbackStatistics(ChartVo chartVo) {
        Map<String, Object> data = chartService.getSalesbackStatistics(chartVo);
        return new DataGridView(data);
    }

    /**
     * 获取库存统计数据
     * @param chartVo 查询条件
     * @return 库存统计数据
     */
    @RequestMapping("/getGoodsStockStatistics")
    public DataGridView getGoodsStockStatistics(ChartVo chartVo) {
        Map<String, Object> data = chartService.getGoodsStockStatistics(chartVo);
        return new DataGridView(data);
    }

    /**
     * 获取客户统计数据
     * @param chartVo 查询条件
     * @return 客户统计数据
     */
    @RequestMapping("/getCustomerStatistics")
    public DataGridView getCustomerStatistics(ChartVo chartVo) {
        Map<String, Object> data = chartService.getCustomerStatistics(chartVo);
        return new DataGridView(data);
    }

    /**
     * 获取仪表板数据
     * @return 仪表板数据
     */
    @RequestMapping("/getDashboardData")
    public DataGridView getDashboardData() {
        Map<String, Object> data = chartService.getDashboardData();
        return new DataGridView(data);
    }
}