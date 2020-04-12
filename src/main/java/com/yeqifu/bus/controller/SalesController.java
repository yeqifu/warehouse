package com.yeqifu.bus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Customer;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.service.ICustomerService;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.ISalesService;
import com.yeqifu.bus.vo.SalesVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * InnoDB free: 9216 kB 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private ISalesService salesService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询所有商品销售信息
     * @param salesVo
     * @return
     */
    @RequestMapping("loadAllSales")
    public DataGridView loadAllSales(SalesVo salesVo){
        IPage<Sales> page = new Page<>(salesVo.getPage(),salesVo.getLimit());
        QueryWrapper<Sales> queryWrapper = new QueryWrapper<Sales>();
        //根据客户进行模糊查询
        queryWrapper.eq(salesVo.getCustomerid()!=null&&salesVo.getCustomerid()!=0,"customerid",salesVo.getCustomerid());
        //根据商品模糊查询
        queryWrapper.eq(salesVo.getGoodsid()!=null&&salesVo.getGoodsid()!=0,"goodsid",salesVo.getGoodsid());
        //根据时间进行模糊查询
        queryWrapper.ge(salesVo.getStartTime()!=null,"salestime",salesVo.getStartTime());
        queryWrapper.le(salesVo.getEndTime()!=null,"salestime",salesVo.getEndTime());
        IPage<Sales> page1 = salesService.page(page, queryWrapper);
        List<Sales> records = page1.getRecords();
        for (Sales sales : records) {
            //设置客户姓名
            Customer customer = customerService.getById(sales.getCustomerid());
            if(null!=customer){
                sales.setCustomername(customer.getCustomername());
            }
            //设置商品名称
            Goods goods = goodsService.getById(sales.getGoodsid());
            if (null!=goods){
                //设置商品名称
                sales.setGoodsname(goods.getGoodsname());
                //设置商品规格
                sales.setSize(goods.getSize());
            }
        }
        return new DataGridView(page1.getTotal(),page1.getRecords());
    }

    /**
     * 添加商品销售信息
     * @param salesVo
     * @return
     */
    @RequestMapping("addSales")
    public ResultObj addSales(SalesVo salesVo){
        try {
            //获得当前系统用户
            User user = (User) WebUtils.getSession().getAttribute("user");
            //设置操作人
            salesVo.setOperateperson(user.getName());
            //设置销售时间
            salesVo.setSalestime(new Date());
            salesService.save(salesVo);
            return ResultObj.ADD_SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 更新商品销售信息
     * @param salesVo
     * @return
     */
    @RequestMapping("updateSales")
    public ResultObj updateSales(SalesVo salesVo){
        try {
            salesService.updateById(salesVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除商品销售信息
     * @param id
     * @return
     */
    @RequestMapping("deleteSales")
    public ResultObj deleteSales(Integer id){
        try {
            salesService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}

