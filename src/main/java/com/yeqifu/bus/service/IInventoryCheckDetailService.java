package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.InventoryCheckDetail;

import java.util.List;

/**
 * <p>
 * 库存盘点明细表 服务类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface IInventoryCheckDetailService extends IService<InventoryCheckDetail> {

    /**
     * 根据盘点单ID查询明细
     * @param checkid 盘点单ID
     * @return 明细列表
     */
    List<InventoryCheckDetail> getDetailsByCheckid(Integer checkid);

    /**
     * 批量保存盘点明细
     * @param details 明细列表
     */
    void batchSaveDetails(List<InventoryCheckDetail> details);

    /**
     * 计算差异
     * @param detail 盘点明细
     * @return 更新后的明细
     */
    InventoryCheckDetail calculateDifference(InventoryCheckDetail detail);

    /**
     * 获取盘点差异统计
     * @param checkid 盘点单ID
     * @return 差异统计列表
     */
    List<InventoryCheckDetail> getDiffStatistics(Integer checkid);

}
