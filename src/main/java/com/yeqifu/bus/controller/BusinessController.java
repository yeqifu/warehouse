package com.yeqifu.bus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 业务管理的路由器
 * @Author: 落亦-
 * @Date: 2019/12/5 9:33
 */
@Controller
@RequestMapping("bus")
public class BusinessController {

    /**
     * 跳转到客户管理页面
     * @return
     */
    @RequestMapping("toCustomerManager")
    public String toCustomerManager(){
        return "business/customer/customerManager";
    }

    /**
     * 跳转到供应商管理页面
     * @return
     */
    @RequestMapping("toProviderManager")
    public String toProviderManager(){
        return "business/provider/providerManager";
    }

    /**
     * 跳转到商品管理页面
     * @return
     */
    @RequestMapping("toGoodsManager")
    public String toGoodsManager(){
        return "business/goods/goodsManager";
    }

    /**
     * 跳转到进货管理页面
     * @return
     */
    @RequestMapping("toInportManager")
    public String toInportManager(){
        return "business/inport/inportManager";
    }

    /**
     * 跳转到退货管理页面
     * @return
     */
    @RequestMapping("toOutportManager")
    public String toOutportManager(){
        return "business/outport/outportManager";
    }

    /**
     * 跳转到商品销售管理页面
     * @return
     */
    @RequestMapping("toSalesManager")
    public String toSalesManager(){
        return "business/sales/salesManager";
    }

    /**
     * 跳转到商品销售管理页面
     * @return
     */
    @RequestMapping("toSalesbackManager")
    public String toSalesbackManager(){
        return "business/salesback/salesbackManager";
    }

}
