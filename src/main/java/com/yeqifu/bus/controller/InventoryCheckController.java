package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckItem;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.IInventoryCheckService;
import com.yeqifu.bus.vo.InventoryCheckVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventoryCheck")
public class InventoryCheckController {

    @Autowired
    private IInventoryCheckService inventoryCheckService;

    @Autowired
    private IGoodsService goodsService;

    @RequestMapping("loadAllCheck")
    public DataGridView loadAllCheck(InventoryCheckVo vo) {
        IPage<InventoryCheck> page = new Page<>(vo.getPage(), vo.getLimit());
        QueryWrapper<InventoryCheck> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(vo.getCheckNo()), "check_no", vo.getCheckNo());
        wrapper.eq(vo.getStatus() != null, "status", vo.getStatus());
        wrapper.eq(StringUtils.isNotBlank(vo.getCheckType()), "check_type", vo.getCheckType());
        wrapper.orderByDesc("create_time");
        inventoryCheckService.page(page, wrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    @RequestMapping("getCheckById")
    public DataGridView getCheckById(Integer id) {
        InventoryCheck check = inventoryCheckService.getCheckWithItems(id);
        return new DataGridView(check);
    }

    @RequestMapping("addCheck")
    public ResultObj addCheck(InventoryCheckVo vo) {
        try {
            List<InventoryCheckItem> items = generateItems(vo.getWarehouseId(), vo.getCategoryName());
            inventoryCheckService.createCheck(vo, items);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    @RequestMapping("updateCheckItems")
    public ResultObj updateCheckItems(Integer checkId, String itemsJson) {
        try {
            List<InventoryCheckItem> items = parseItemsJson(itemsJson);
            inventoryCheckService.updateCheckItems(checkId, items);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    @RequestMapping("completeCheck")
    public ResultObj completeCheck(Integer id) {
        try {
            inventoryCheckService.completeCheck(id);
            return new ResultObj(200, "盘点完成，库存已更新");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(500, "盘点完成失败");
        }
    }

    @RequestMapping("cancelCheck")
    public ResultObj cancelCheck(Integer id) {
        try {
            inventoryCheckService.cancelCheck(id);
            return new ResultObj(200, "盘点已取消");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(500, "取消盘点失败");
        }
    }

    @RequestMapping("deleteCheck")
    public ResultObj deleteCheck(Integer id) {
        try {
            inventoryCheckService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    @RequestMapping("getDifferenceReport")
    public DataGridView getDifferenceReport(Integer checkId) {
        List<InventoryCheckItem> items = inventoryCheckService.getDifferenceReport(checkId);
        return new DataGridView((long) items.size(), items);
    }

    @RequestMapping("loadGoodsForCheck")
    public DataGridView loadGoodsForCheck(Integer warehouseId, String categoryName) {
        List<InventoryCheckItem> items = generateItems(warehouseId, categoryName);
        return new DataGridView((long) items.size(), items);
    }

    private List<InventoryCheckItem> generateItems(Integer warehouseId, String categoryName) {
        List<InventoryCheckItem> items = new ArrayList<>();
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("available", 1);
        List<Goods> goodsList = goodsService.list(wrapper);
        for (Goods goods : goodsList) {
            InventoryCheckItem item = new InventoryCheckItem();
            item.setGoodsid(goods.getId());
            item.setGoodsname(goods.getGoodsname());
            item.setProductcode(goods.getProductcode());
            item.setSystemNumber(goods.getNumber());
            item.setActualNumber(goods.getNumber());
            item.setDifferenceNumber(0);
            items.add(item);
        }
        return items;
    }

    private List<InventoryCheckItem> parseItemsJson(String itemsJson) {
        List<InventoryCheckItem> items = new ArrayList<>();
        if (StringUtils.isNotBlank(itemsJson)) {
            com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(itemsJson);
            for (int i = 0; i < jsonArray.size(); i++) {
                com.alibaba.fastjson.JSONObject obj = jsonArray.getJSONObject(i);
                InventoryCheckItem item = new InventoryCheckItem();
                item.setGoodsid(obj.getInteger("goodsid"));
                item.setSystemNumber(obj.getInteger("systemNumber"));
                item.setActualNumber(obj.getInteger("actualNumber"));
                if (item.getActualNumber() != null && item.getSystemNumber() != null) {
                    item.setDifferenceNumber(item.getActualNumber() - item.getSystemNumber());
                }
                item.setRemark(obj.getString("remark"));
                items.add(item);
            }
        }
        return items;
    }
}
