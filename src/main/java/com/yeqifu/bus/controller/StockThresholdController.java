package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.StockThreshold;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.IStockThresholdService;
import com.yeqifu.bus.vo.StockThresholdVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品库存阈值设置 前端控制器
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@RestController
@RequestMapping("/stockThreshold")
public class StockThresholdController {

    @Autowired
    private IStockThresholdService stockThresholdService;

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询库存阈值设置列表
     * @param stockThresholdVo
     * @return
     */
    @RequestMapping("loadAllStockThreshold")
    public DataGridView loadAllStockThreshold(StockThresholdVo stockThresholdVo) {
        IPage<StockThreshold> page = new Page<>(stockThresholdVo.getPage(), stockThresholdVo.getLimit());
        QueryWrapper<StockThreshold> queryWrapper = new QueryWrapper<>();

        // 获取所有阈值设置
        stockThresholdService.page(page, queryWrapper);

        List<StockThreshold> records = page.getRecords();
        for (StockThreshold record : records) {
            Goods goods = goodsService.getById(record.getGoodsid());
            if (goods != null) {
                record.setGoodsname(goods.getGoodsname());
                record.setSize(goods.getSize());
                record.setCurrentstock(goods.getNumber());
            }
        }

        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 添加库存阈值设置
     * @param stockThresholdVo
     * @return
     */
    @RequestMapping("addStockThreshold")
    public ResultObj addStockThreshold(StockThresholdVo stockThresholdVo) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            stockThresholdVo.setOperateperson(user.getName());
            stockThresholdVo.setCreatetime(new Date());
            stockThresholdVo.setUpdatetime(new Date());
            stockThresholdService.saveOrUpdateThreshold(stockThresholdVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改库存阈值设置
     * @param stockThresholdVo
     * @return
     */
    @RequestMapping("updateStockThreshold")
    public ResultObj updateStockThreshold(StockThresholdVo stockThresholdVo) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            stockThresholdVo.setOperateperson(user.getName());
            stockThresholdVo.setUpdatetime(new Date());
            stockThresholdService.saveOrUpdateThreshold(stockThresholdVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除库存阈值设置
     * @param id
     * @return
     */
    @RequestMapping("deleteStockThreshold")
    public ResultObj deleteStockThreshold(Integer id) {
        try {
            stockThresholdService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除库存阈值设置
     * @param ids
     * @return
     */
    @RequestMapping("batchDeleteStockThreshold")
    public ResultObj batchDeleteStockThreshold(Integer[] ids) {
        try {
            List<Integer> idList = new ArrayList<>();
            for (Integer id : ids) {
                idList.add(id);
            }
            stockThresholdService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 根据商品ID查询阈值设置
     * @param goodsid
     * @return
     */
    @RequestMapping("getThresholdByGoodsid")
    public DataGridView getThresholdByGoodsid(Integer goodsid) {
        StockThreshold threshold = stockThresholdService.getByGoodsid(goodsid);
        if (threshold != null) {
            Goods goods = goodsService.getById(threshold.getGoodsid());
            if (goods != null) {
                threshold.setGoodsname(goods.getGoodsname());
                threshold.setSize(goods.getSize());
                threshold.setCurrentstock(goods.getNumber());
            }
        }
        return new DataGridView(threshold);
    }

    /**
     * 获取需要预警的商品列表
     * @return
     */
    @RequestMapping("getWarningGoods")
    public DataGridView getWarningGoods() {
        List<StockThreshold> list = stockThresholdService.getWarningGoods();
        for (StockThreshold record : list) {
            Goods goods = goodsService.getById(record.getGoodsid());
            if (goods != null) {
                record.setGoodsname(goods.getGoodsname());
                record.setSize(goods.getSize());
                record.setCurrentstock(goods.getNumber());
            }
        }
        return new DataGridView((long) list.size(), list);
    }

    /**
     * 检查商品库存预警状态
     * @param goodsid
     * @return
     */
    @RequestMapping("checkStockWarning")
    public ResultObj checkStockWarning(Integer goodsid) {
        Integer warningType = stockThresholdService.checkStockWarning(goodsid);
        String message;
        switch (warningType) {
            case 0:
                message = "库存正常";
                break;
            case 1:
                message = "库存不足预警";
                break;
            case 2:
                message = "库存积压预警";
                break;
            default:
                message = "未知状态";
        }
        return new ResultObj(200, message);
    }

    /**
     * 批量设置库存阈值
     * @param goodsids
     * @param minstock
     * @param maxstock
     * @param warningenabled
     * @param warningmethods
     * @return
     */
    @RequestMapping("batchSetThreshold")
    public ResultObj batchSetThreshold(Integer[] goodsids, Integer minstock, Integer maxstock,
                                       Integer warningenabled, String warningmethods) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            List<Integer> goodsidList = new ArrayList<>();
            for (Integer goodsid : goodsids) {
                goodsidList.add(goodsid);
            }
            stockThresholdService.batchSetThreshold(goodsidList, minstock, maxstock,
                    warningenabled, warningmethods, user.getName());
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

}
