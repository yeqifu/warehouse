package com.yeqifu.bus.service;

import com.yeqifu.bus.entity.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 仓库表 服务类
 * </p>
 *
 * @author luoyi-
 * @since 2023-10-05
 */
public interface IWarehouseService extends IService<Warehouse> {

    /**
     * 根据仓库id删除仓库
     * @param id    仓库id
     */
    void deleteWarehouseById(Integer id);
}