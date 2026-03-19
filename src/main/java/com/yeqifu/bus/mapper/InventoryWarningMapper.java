package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryWarning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 库存预警记录表 Mapper 接口
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface InventoryWarningMapper extends BaseMapper<InventoryWarning> {

    /**
     * 查询指定商品的未处理预警记录
     * @param goodsid 商品ID
     * @param warningtype 预警类型
     * @return 预警记录列表
     */
    List<InventoryWarning> selectUnhandledByGoodsid(@Param("goodsid") Integer goodsid, @Param("warningtype") Integer warningtype);

    /**
     * 统计未处理的预警数量
     * @return 预警数量
     */
    Integer countUnhandledWarning();

}
