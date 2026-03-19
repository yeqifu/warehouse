package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 库存盘点单表 Mapper 接口
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface InventoryCheckMapper extends BaseMapper<InventoryCheck> {

    /**
     * 根据盘点单号查询盘点单
     * @param checkno 盘点单号
     * @return 盘点单
     */
    InventoryCheck selectByCheckno(@Param("checkno") String checkno);

    /**
     * 查询进行中的盘点单
     * @return 盘点单列表
     */
    List<InventoryCheck> selectInProgressChecks();

}
