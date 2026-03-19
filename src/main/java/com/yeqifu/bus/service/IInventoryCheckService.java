package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckDetail;

import java.util.List;

/**
 * <p>
 * 库存盘点单表 服务类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface IInventoryCheckService extends IService<InventoryCheck> {

    /**
     * 生成盘点单
     * @param inventoryCheck 盘点单信息
     * @return 生成的盘点单
     */
    InventoryCheck generateCheckOrder(InventoryCheck inventoryCheck);

    /**
     * 开始盘点
     * @param id 盘点单ID
     * @param checkperson 盘点人
     */
    void startCheck(Integer id, String checkperson);

    /**
     * 完成盘点
     * @param id 盘点单ID
     */
    void completeCheck(Integer id);

    /**
     * 提交盘点明细
     * @param details 盘点明细列表
     */
    void submitCheckDetails(List<InventoryCheckDetail> details);

    /**
     * 生成盘盈盘亏报表
     * @param checkid 盘点单ID
     * @return 报表数据
     */
    List<InventoryCheckDetail> generateProfitLossReport(Integer checkid);

    /**
     * 根据仓库生成盘点单
     * @param warehouseid 仓库ID
     * @param createperson 创建人
     * @param remark 备注
     * @return 盘点单
     */
    InventoryCheck generateCheckByWarehouse(Integer warehouseid, String createperson, String remark);

    /**
     * 根据品类生成盘点单
     * @param categoryid 品类ID
     * @param createperson 创建人
     * @param remark 备注
     * @return 盘点单
     */
    InventoryCheck generateCheckByCategory(Integer categoryid, String createperson, String remark);

    /**
     * 生成全盘盘点单
     * @param createperson 创建人
     * @param remark 备注
     * @return 盘点单
     */
    InventoryCheck generateFullCheck(String createperson, String remark);

}
