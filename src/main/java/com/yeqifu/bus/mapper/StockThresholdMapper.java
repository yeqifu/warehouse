package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.StockThreshold;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品库存阈值设置表 Mapper 接口
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface StockThresholdMapper extends BaseMapper<StockThreshold> {

    /**
     * 根据商品ID查询阈值设置
     * @param goodsid 商品ID
     * @return 阈值设置
     */
    StockThreshold selectByGoodsid(@Param("goodsid") Integer goodsid);

    /**
     * 查询所有启用了预警的商品阈值设置
     * @return 阈值设置列表
     */
    List<StockThreshold> selectAllEnabledThresholds();

    /**
     * 查询需要预警的商品
     * @return 阈值设置列表
     */
    List<StockThreshold> selectWarningGoods();

}
