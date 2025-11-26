package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.InventoryBalance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品库存现存量表 服务类
 * </p>
 *
 * @author yeqifu
 * @since 2023-05-10
 */
public interface IInventoryBalanceService extends IService<InventoryBalance> {

    /**
     * 根据库存id删除库存
     * @param id    库存id
     */
    void deleteInventoryBalanceById(Long id);
}