package com.yeqifu.bus.mapper;

import com.yeqifu.bus.entity.Salesback;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-23
 */
public interface SalesbackMapper extends BaseMapper<Salesback> {

    /**
     * 获取退货总额
     * @return
     */
    Double getTotalSalesback();

    /**
     * 获取退货商品数量
     * @return
     */
    Integer getSalesbackCount();

    /**
     * 获取按月份统计的退货数据
     * @return
     */
    List<Map<String, Object>> getSalesbackByMonth();
}
