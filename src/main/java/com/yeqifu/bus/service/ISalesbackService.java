package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.Salesback;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-23
 */
public interface ISalesbackService extends IService<Salesback> {

    /**
     * 对商品销售进行退货处理
     * @param id    销售单ID
     * @param number    退货数量
     * @param remark    备注
     */
    void addSalesback(Integer id, Integer number, String remark);

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
