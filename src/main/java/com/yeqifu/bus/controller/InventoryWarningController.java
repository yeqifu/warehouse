package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryWarning;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.IInventoryWarningService;
import com.yeqifu.bus.vo.InventoryWarningVo;
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
 * 库存预警记录 前端控制器
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@RestController
@RequestMapping("/inventoryWarning")
public class InventoryWarningController {

    @Autowired
    private IInventoryWarningService inventoryWarningService;

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询库存预警记录列表
     * @param inventoryWarningVo
     * @return
     */
    @RequestMapping("loadAllInventoryWarning")
    public DataGridView loadAllInventoryWarning(InventoryWarningVo inventoryWarningVo) {
        IPage<InventoryWarning> page = new Page<>(inventoryWarningVo.getPage(), inventoryWarningVo.getLimit());
        QueryWrapper<InventoryWarning> queryWrapper = new QueryWrapper<>();

        // 按预警类型查询
        queryWrapper.eq(inventoryWarningVo.getWarningtype() != null, "warningtype", inventoryWarningVo.getWarningtype());
        // 按状态查询
        queryWrapper.eq(inventoryWarningVo.getStatus() != null, "status", inventoryWarningVo.getStatus());
        // 按时间范围查询
        queryWrapper.ge(StringUtils.isNotBlank(inventoryWarningVo.getStarttime()), "warningtime", inventoryWarningVo.getStarttime());
        queryWrapper.le(StringUtils.isNotBlank(inventoryWarningVo.getEndtime()), "warningtime", inventoryWarningVo.getEndtime());

        queryWrapper.orderByDesc("warningtime");
        inventoryWarningService.page(page, queryWrapper);

        List<InventoryWarning> records = page.getRecords();
        for (InventoryWarning record : records) {
            Goods goods = goodsService.getById(record.getGoodsid());
            if (goods != null) {
                record.setGoodsname(goods.getGoodsname());
                record.setSize(goods.getSize());
            }
        }

        return new DataGridView(page.getTotal(), records);
    }

    /**
     * 手动触发库存预警检查
     * @return
     */
    @RequestMapping("checkInventoryWarning")
    public ResultObj checkInventoryWarning() {
        try {
            inventoryWarningService.checkInventoryAndGenerateWarnings();
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 处理预警记录
     * @param id
     * @param remark
     * @return
     */
    @RequestMapping("handleWarning")
    public ResultObj handleWarning(Integer id, String remark) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inventoryWarningService.handleWarning(id, user.getName(), remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 批量处理预警记录
     * @param ids
     * @param remark
     * @return
     */
    @RequestMapping("batchHandleWarning")
    public ResultObj batchHandleWarning(Integer[] ids, String remark) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            List<Integer> idList = new ArrayList<>();
            for (Integer id : ids) {
                idList.add(id);
            }
            inventoryWarningService.batchHandleWarnings(idList, user.getName(), remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 获取未处理预警数量
     * @return
     */
    @RequestMapping("getUnhandledWarningCount")
    public DataGridView getUnhandledWarningCount() {
        Integer count = inventoryWarningService.getUnhandledWarningCount();
        return new DataGridView(count);
    }

    /**
     * 删除预警记录
     * @param id
     * @return
     */
    @RequestMapping("deleteInventoryWarning")
    public ResultObj deleteInventoryWarning(Integer id) {
        try {
            inventoryWarningService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除预警记录
     * @param ids
     * @return
     */
    @RequestMapping("batchDeleteInventoryWarning")
    public ResultObj batchDeleteInventoryWarning(Integer[] ids) {
        try {
            List<Integer> idList = new ArrayList<>();
            for (Integer id : ids) {
                idList.add(id);
            }
            inventoryWarningService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}
