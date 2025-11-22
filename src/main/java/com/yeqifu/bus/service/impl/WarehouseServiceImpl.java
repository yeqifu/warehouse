package com.yeqifu.bus.service.impl;

import com.yeqifu.bus.entity.Warehouse;
import com.yeqifu.bus.mapper.WarehouseMapper;
import com.yeqifu.bus.service.IWarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 仓库表 服务实现类
 * </p>
 *
 * @author yeqifu
 * @since 2023-05-10
 */
@Service
@Transactional
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements IWarehouseService {

    @Override
    public boolean save(Warehouse entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(Warehouse entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public Warehouse getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    /**
     * 根据仓库id删除仓库
     * @param id    仓库id
     */
    @Override
    public void deleteWarehouseById(Integer id) {
        //目前仓库表没有关联其他表，直接删除即可
        this.removeById(id);
    }
}