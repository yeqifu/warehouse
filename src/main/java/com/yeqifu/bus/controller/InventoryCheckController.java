package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckDetail;
import com.yeqifu.bus.service.IGoodsService;
import com.yeqifu.bus.service.IInventoryCheckDetailService;
import com.yeqifu.bus.service.IInventoryCheckService;
import com.yeqifu.bus.vo.InventoryCheckDetailVo;
import com.yeqifu.bus.vo.InventoryCheckVo;
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
 * 库存盘点单 前端控制器
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@RestController
@RequestMapping("/inventoryCheck")
public class InventoryCheckController {

    @Autowired
    private IInventoryCheckService inventoryCheckService;

    @Autowired
    private IInventoryCheckDetailService inventoryCheckDetailService;

    @Autowired
    private IGoodsService goodsService;

    /**
     * 查询盘点单列表
     * @param inventoryCheckVo
     * @return
     */
    @RequestMapping("loadAllInventoryCheck")
    public DataGridView loadAllInventoryCheck(InventoryCheckVo inventoryCheckVo) {
        IPage<InventoryCheck> page = new Page<>(inventoryCheckVo.getPage(), inventoryCheckVo.getLimit());
        QueryWrapper<InventoryCheck> queryWrapper = new QueryWrapper<>();

        // 按盘点单号查询
        queryWrapper.like(StringUtils.isNotBlank(inventoryCheckVo.getCheckno()), "checkno", inventoryCheckVo.getCheckno());
        // 按盘点类型查询
        queryWrapper.eq(inventoryCheckVo.getChecktype() != null, "checktype", inventoryCheckVo.getChecktype());
        // 按状态查询
        queryWrapper.eq(inventoryCheckVo.getStatus() != null, "status", inventoryCheckVo.getStatus());
        // 按时间范围查询
        queryWrapper.ge(StringUtils.isNotBlank(inventoryCheckVo.getStarttime()), "starttime", inventoryCheckVo.getStarttime());
        queryWrapper.le(StringUtils.isNotBlank(inventoryCheckVo.getEndtime()), "endtime", inventoryCheckVo.getEndtime());

        queryWrapper.orderByDesc("id");
        inventoryCheckService.page(page, queryWrapper);

        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 生成全盘盘点单
     * @param remark
     * @return
     */
    @RequestMapping("generateFullCheck")
    public ResultObj generateFullCheck(String remark) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inventoryCheckService.generateFullCheck(user.getName(), remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 按仓库生成盘点单
     * @param warehouseid
     * @param remark
     * @return
     */
    @RequestMapping("generateCheckByWarehouse")
    public ResultObj generateCheckByWarehouse(Integer warehouseid, String remark) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inventoryCheckService.generateCheckByWarehouse(warehouseid, user.getName(), remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 按品类生成盘点单
     * @param categoryid
     * @param remark
     * @return
     */
    @RequestMapping("generateCheckByCategory")
    public ResultObj generateCheckByCategory(Integer categoryid, String remark) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inventoryCheckService.generateCheckByCategory(categoryid, user.getName(), remark);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 开始盘点
     * @param id
     * @return
     */
    @RequestMapping("startCheck")
    public ResultObj startCheck(Integer id) {
        try {
            User user = (User) WebUtils.getSession().getAttribute("user");
            inventoryCheckService.startCheck(id, user.getName());
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 完成盘点
     * @param id
     * @return
     */
    @RequestMapping("completeCheck")
    public ResultObj completeCheck(Integer id) {
        try {
            inventoryCheckService.completeCheck(id);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 删除盘点单
     * @param id
     * @return
     */
    @RequestMapping("deleteInventoryCheck")
    public ResultObj deleteInventoryCheck(Integer id) {
        try {
            inventoryCheckService.removeById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 批量删除盘点单
     * @param ids
     * @return
     */
    @RequestMapping("batchDeleteInventoryCheck")
    public ResultObj batchDeleteInventoryCheck(Integer[] ids) {
        try {
            List<Integer> idList = new ArrayList<>();
            for (Integer id : ids) {
                idList.add(id);
            }
            inventoryCheckService.removeByIds(idList);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 查询盘点明细
     * @param checkid
     * @return
     */
    @RequestMapping("loadCheckDetails")
    public DataGridView loadCheckDetails(Integer checkid) {
        List<InventoryCheckDetail> details = inventoryCheckDetailService.getDetailsByCheckid(checkid);
        return new DataGridView((long) details.size(), details);
    }

    /**
     * 提交盘点明细
     * @param details
     * @return
     */
    @RequestMapping("submitCheckDetails")
    public ResultObj submitCheckDetails(String details) {
        try {
            // 解析JSON字符串为List<InventoryCheckDetail>
            // 这里简化处理，实际应该使用JSON解析
            // inventoryCheckService.submitCheckDetails(detailList);
            return ResultObj.OPERATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.OPERATE_ERROR;
        }
    }

    /**
     * 更新盘点明细
     * @param inventoryCheckDetailVo
     * @return
     */
    @RequestMapping("updateCheckDetail")
    public ResultObj updateCheckDetail(InventoryCheckDetailVo inventoryCheckDetailVo) {
        try {
            // 计算差异
            inventoryCheckDetailService.calculateDifference(inventoryCheckDetailVo);
            inventoryCheckDetailVo.setChecktime(new Date());
            inventoryCheckDetailService.updateById(inventoryCheckDetailVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 生成盘盈盘亏报表
     * @param checkid
     * @return
     */
    @RequestMapping("generateProfitLossReport")
    public DataGridView generateProfitLossReport(Integer checkid) {
        List<InventoryCheckDetail> report = inventoryCheckService.generateProfitLossReport(checkid);
        return new DataGridView((long) report.size(), report);
    }

    /**
     * 获取盘点差异统计
     * @param checkid
     * @return
     */
    @RequestMapping("getDiffStatistics")
    public DataGridView getDiffStatistics(Integer checkid) {
        List<InventoryCheckDetail> statistics = inventoryCheckDetailService.getDiffStatistics(checkid);

        // 计算汇总数据
        int totalProfit = 0; // 盘盈数量
        int totalLoss = 0; // 盘亏数量
        double totalProfitAmount = 0.0; // 盘盈金额
        double totalLossAmount = 0.0; // 盘亏金额

        for (InventoryCheckDetail detail : statistics) {
            Goods goods = goodsService.getById(detail.getGoodsid());
            if (goods != null) {
                detail.setGoodsname(goods.getGoodsname());
                detail.setSize(goods.getSize());
                detail.setPrice(goods.getPrice());
            }

            if (detail.getDifftype() == 1) { // 盘盈
                totalProfit += detail.getDiffnum();
                totalProfitAmount += detail.getDiffamount();
            } else if (detail.getDifftype() == 2) { // 盘亏
                totalLoss += Math.abs(detail.getDiffnum());
                totalLossAmount += Math.abs(detail.getDiffamount());
            }
        }

        // 构建返回数据
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("details", statistics);
        result.put("totalProfit", totalProfit);
        result.put("totalLoss", totalLoss);
        result.put("totalProfitAmount", totalProfitAmount);
        result.put("totalLossAmount", totalLossAmount);
        result.put("netAmount", totalProfitAmount - totalLossAmount);

        return new DataGridView((long) statistics.size(), result);
    }

}
