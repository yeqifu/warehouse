package com.yeqifu.bus.controller;

import com.yeqifu.bus.service.ISalesService;
import com.yeqifu.bus.service.ISalesbackService;
import com.yeqifu.sys.common.DataGridView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据可视化控制器
 * @author luoyi-
 * @since 2026-01-24
 */
@RestController
@RequestMapping("/visualization")
public class VisualizationController {

    @Autowired
    private ISalesService salesService;

    @Autowired
    private ISalesbackService salesbackService;

    /**
     * 获取销售统计数据
     * @return
     */
    @RequestMapping("getSalesStatistics")
    public DataGridView getSalesStatistics() {
        Map<String, Object> result = new HashMap<>();
        // 获取销售总额
        Double totalSales = salesService.getTotalSales();
        // 获取退货总额
        Double totalSalesback = salesbackService.getTotalSalesback();
        // 获取销售商品数量
        Integer salesCount = salesService.getSalesCount();
        // 获取退货商品数量
        Integer salesbackCount = salesbackService.getSalesbackCount();
        
        result.put("totalSales", totalSales);
        result.put("totalSalesback", totalSalesback);
        result.put("salesCount", salesCount);
        result.put("salesbackCount", salesbackCount);
        
        return new DataGridView(result);
    }

    /**
     * 获取按月份统计的销售数据
     * @return
     */
    @RequestMapping("getSalesByMonth")
    public DataGridView getSalesByMonth() {
        return new DataGridView(salesService.getSalesByMonth());
    }

    /**
     * 获取按商品分类统计的销售数据
     * @return
     */
    @RequestMapping("getSalesByCategory")
    public DataGridView getSalesByCategory() {
        return new DataGridView(salesService.getSalesByCategory());
    }

    /**
     * 获取按客户统计的销售数据
     * @return
     */
    @RequestMapping("getSalesByCustomer")
    public DataGridView getSalesByCustomer() {
        return new DataGridView(salesService.getSalesByCustomer());
    }

    /**
     * 获取按月份统计的退货数据
     * @return
     */
    @RequestMapping("getSalesbackByMonth")
    public DataGridView getSalesbackByMonth() {
        return new DataGridView(salesbackService.getSalesbackByMonth());
    }
}