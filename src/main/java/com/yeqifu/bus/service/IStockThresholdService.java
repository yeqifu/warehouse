package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.StockThreshold;

import java.util.List;

/**
 * <p>
 * 商品库存阈值设置表 服务类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface IStockThresholdService extends IService<StockThreshold> {

    /**
     * 根据商品ID查询阈值设置
     * @param goodsid 商品ID
     * @return 阈值设置
     */
    StockThreshold getByGoodsid(Integer goodsid);

    /**
     * 保存或更新阈值设置
     * @param stockThreshold 阈值设置
     */
    void saveOrUpdateThreshold(StockThreshold stockThreshold);

    /**
     * 批量设置阈值
     * @param goodsids 商品ID列表
     * @param minstock 最低库存
     * @param maxstock 最高库存
     * @param warningenabled 是否启用预警
     * @param warningmethods 预警方式
     * @param operateperson 操作人
     */
    void batchSetThreshold(List<Integer> goodsids, Integer minstock, Integer maxstock,
                          Integer warningenabled, String warningmethods, String operateperson);

    /**
     * 获取需要预警的商品列表
     * @return 阈值设置列表
     */
    List<StockThreshold> getWarningGoods();

    /**
     * 检查商品库存是否需要预警
     * @param goodsid 商品ID
     * @return 预警类型：0-正常，1-库存不足，2-库存积压
     */
    Integer checkStockWarning(Integer goodsid);

}
