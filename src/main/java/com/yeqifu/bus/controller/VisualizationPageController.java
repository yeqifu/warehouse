package com.yeqifu.bus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据可视化页面控制器
 * @author luoyi-
 * @since 2026-01-24
 */
@Controller
@RequestMapping("/visualization")
public class VisualizationPageController {

    /**
     * 跳转到数据可视化页面
     * @return
     */
    @RequestMapping("toVisualizationManager")
    public String toVisualizationManager() {
        return "business/visualization/visualizationManager";
    }
}