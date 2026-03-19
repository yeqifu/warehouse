package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.InventoryWarning;
import com.yeqifu.bus.service.IInventoryWarningService;
import com.yeqifu.bus.vo.InventoryWarningVo;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventoryWarning")
public class InventoryWarningController {

    @Autowired
    private IInventoryWarningService inventoryWarningService;

    @RequestMapping("loadAllWarning")
    public DataGridView loadAllWarning(InventoryWarningVo vo) {
        IPage<InventoryWarning> page = new Page<>(vo.getPage(), vo.getLimit());
        QueryWrapper<InventoryWarning> wrapper = new QueryWrapper<>();
        wrapper.eq(vo.getStatus() != null, "status", vo.getStatus());
        wrapper.eq(vo.getType() != null, "type", vo.getType());
        wrapper.orderByDesc("create_time");
        inventoryWarningService.page(page, wrapper);
        List<InventoryWarning> records = page.getRecords();
        for (InventoryWarning warning : records) {
            inventoryWarningService.getById(warning.getId());
        }
        return new DataGridView(page.getTotal(), records);
    }

    @RequestMapping("getUnhandledWarnings")
    public DataGridView getUnhandledWarnings() {
        List<InventoryWarning> list = inventoryWarningService.getUnhandledWarnings();
        return new DataGridView((long) list.size(), list);
    }

    @RequestMapping("handleWarning")
    public ResultObj handleWarning(InventoryWarningVo vo) {
        try {
            inventoryWarningService.handleWarning(vo.getId(), vo.getHandler(), vo.getHandleRemark());
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    @RequestMapping("checkWarnings")
    public ResultObj checkWarnings() {
        try {
            inventoryWarningService.checkAndCreateWarnings();
            return new ResultObj(200, "库存检查完成");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultObj(500, "库存检查失败");
        }
    }

    @RequestMapping("deleteWarning")
    public ResultObj deleteWarning(Integer id) {
        try {
            inventoryWarningService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }
}
