package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.InventoryBalance;
import com.yeqifu.bus.service.IInventoryBalanceService;
import com.yeqifu.bus.vo.InventoryBalanceVo;
import com.yeqifu.sys.common.Constast;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 商品库存现存量表 前端控制器
 * </p>
 *
 * @author yeqifu
 * @since 2023-05-10
 */
@RestController
@RequestMapping("/inventorybalance")
public class InventoryBalanceController {

    @Autowired
    private IInventoryBalanceService inventoryBalanceService;

    /**
     * 查询所有的库存
     * @param inventoryBalanceVo
     * @return
     */
    @RequestMapping("loadAllInventoryBalance")
    public DataGridView loadAllInventoryBalance(InventoryBalanceVo inventoryBalanceVo){
        //1.声明一个分页page对象
        IPage<InventoryBalance> page = new Page<InventoryBalance>(inventoryBalanceVo.getPage(), inventoryBalanceVo.getLimit());
        //2.声明一个queryWrapper
        QueryWrapper<InventoryBalance> queryWrapper = new QueryWrapper<InventoryBalance>();
        
        // Basic query conditions
        queryWrapper.eq(inventoryBalanceVo.getWarehouseId() != null, "warehouse_id", inventoryBalanceVo.getWarehouseId());
        queryWrapper.eq(inventoryBalanceVo.getGoodsId() != null, "goods_id", inventoryBalanceVo.getGoodsId());
        queryWrapper.eq(inventoryBalanceVo.getStatus() != null, "status", inventoryBalanceVo.getStatus());
        
        // Fuzzy query conditions
        queryWrapper.like(StringUtils.isNotBlank(inventoryBalanceVo.getBatchNo()), "batch_no", inventoryBalanceVo.getBatchNo());
        queryWrapper.like(StringUtils.isNotBlank(inventoryBalanceVo.getLocCode()), "loc_code", inventoryBalanceVo.getLocCode());
        queryWrapper.like(StringUtils.isNotBlank(inventoryBalanceVo.getUom()), "uom", inventoryBalanceVo.getUom());
        
        // Sorting - we'll handle this in the frontend, but we can add default sorting here
        queryWrapper.orderByDesc("updated_at");
        
        inventoryBalanceService.page(page, queryWrapper);
        return new DataGridView(page.getTotal(), page.getRecords());
    }

    /**
     * 添加一个库存
     * @param inventoryBalanceVo
     * @return
     */
    @RequestMapping("addInventoryBalance")
    public ResultObj addInventoryBalance(InventoryBalanceVo inventoryBalanceVo){
        try {
            inventoryBalanceService.save(inventoryBalanceVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改一个库存
     * @param inventoryBalanceVo
     * @return
     */
    @RequestMapping("updateInventoryBalance")
    public ResultObj updateInventoryBalance(InventoryBalanceVo inventoryBalanceVo){
        try {
            inventoryBalanceService.updateById(inventoryBalanceVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除一个库存
     * @param id 库存的ID
     * @return
     */
    @ApiOperation(value = "删除一个库存", notes = "删除一个库存")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "库存ID", required = true, paramType = "query", dataType = "Long")})
    @RequestMapping(value = "deleteInventoryBalance", method = RequestMethod.DELETE)
    public ResultObj deleteInventoryBalance(Long id){
        try {
            inventoryBalanceService.deleteInventoryBalanceById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

}