package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryCheckDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 库存盘点明细表 Mapper 接口
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface InventoryCheckDetailMapper extends BaseMapper<InventoryCheckDetail> {

    /**
     * 根据盘点单ID查询明细
     * @param checkid 盘点单ID
     * @return 明细列表
     */
    List<InventoryCheckDetail> selectByCheckid(@Param("checkid") Integer checkid);

    /**
     * 查询盘点差异统计
     * @param checkid 盘点单ID
     * @return 差异统计
     */
    List<InventoryCheckDetail> selectDiffStatistics(@Param("checkid") Integer checkid);

    /**
     * 批量插入盘点明细
     * @param details 明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<InventoryCheckDetail> details);

}
