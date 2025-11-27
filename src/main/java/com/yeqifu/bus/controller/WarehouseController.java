package com.yeqifu.bus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yeqifu.bus.entity.Warehouse;
import com.yeqifu.bus.service.IWarehouseService;
import com.yeqifu.bus.vo.WarehouseVo;
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
 * 仓库表 前端控制器
 * </p>
 *
 * @author luoyi-
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private IWarehouseService warehouseService;

    /**
     * 查询所有的仓库
     * @param warehouseVo
     * @return
     */
    @RequestMapping("loadAllWarehouse")
    public DataGridView loadAllWarehouse(WarehouseVo warehouseVo){
        //1.声明一个分页page对象
        IPage<Warehouse> page = new Page<Warehouse>(warehouseVo.getPage(),warehouseVo.getLimit());
        //2.声明一个queryWrapper
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
        queryWrapper.like(StringUtils.isNotBlank(warehouseVo.getWarehousename()),"warehousename",warehouseVo.getWarehousename());
        queryWrapper.like(StringUtils.isNotBlank(warehouseVo.getManager()),"manager",warehouseVo.getManager());
        queryWrapper.like(StringUtils.isNotBlank(warehouseVo.getPhone()),"phone",warehouseVo.getPhone());
        warehouseService.page(page,queryWrapper);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    /**
     * 添加一个仓库
     * @param warehouseVo
     * @return
     */
    @RequestMapping("addWarehouse")
    public ResultObj addWarehouse(WarehouseVo warehouseVo){
        try {
            warehouseService.save(warehouseVo);
            return ResultObj.ADD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.ADD_ERROR;
        }
    }

    /**
     * 修改一个仓库
     * @param warehouseVo
     * @return
     */
    @RequestMapping("updateWarehouse")
    public ResultObj updateWarehouse(WarehouseVo warehouseVo){
        try {
            warehouseService.updateById(warehouseVo);
            return ResultObj.UPDATE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.UPDATE_ERROR;
        }
    }

    /**
     * 删除一个仓库
     * @param id 仓库的ID
     * @return
     */
    @ApiOperation(value = "删除一个仓库",notes = "删除一个仓库")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "仓库ID",required = true,paramType = "query",dataType = "Integer")})
    @RequestMapping(value = "deleteWarehouse",method = RequestMethod.DELETE)
    public ResultObj deleteWarehouse(Integer id){
        try {
            warehouseService.deleteWarehouseById(id);
            return ResultObj.DELETE_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultObj.DELETE_ERROR;
        }
    }

    /**
     * 加载所有仓库的下拉列表
     * @return
     */
    @RequestMapping("loadAllWarehouseForSelect")
    public DataGridView loadAllWarehouseForSelect(){
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<Warehouse>();
        queryWrapper.eq("available", Constast.AVAILABLE_TRUE);
        List<Warehouse> list = warehouseService.list(queryWrapper);
        return new DataGridView(list);
    }

}