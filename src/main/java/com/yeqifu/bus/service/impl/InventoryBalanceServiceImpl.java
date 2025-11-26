package com.yeqifu.bus.service.impl;

import com.yeqifu.bus.entity.InventoryBalance;
import com.yeqifu.bus.mapper.InventoryBalanceMapper;
import com.yeqifu.bus.service.IInventoryBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 商品库存现存量表 服务实现类
 * </p>
 *
 * @author yeqifu
 * @since 2023-05-10
 */
@Service
@Transactional
public class InventoryBalanceServiceImpl extends ServiceImpl<InventoryBalanceMapper, InventoryBalance> implements IInventoryBalanceService {

    @Override
    public boolean save(InventoryBalance entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(InventoryBalance entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public InventoryBalance getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    /**
     * 根据库存id删除库存
     * @param id    库存id
     */
    @Override
    public void deleteInventoryBalanceById(Long id) {
        //目前库存表没有关联其他表，直接删除即可
        this.removeById(id);
    }
}