package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckItem;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InventoryCheckItemMapper;
import com.yeqifu.bus.mapper.InventoryCheckMapper;
import com.yeqifu.bus.service.IInventoryCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheck> implements IInventoryCheckService {

    @Autowired
    private InventoryCheckItemMapper itemMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<InventoryCheck> getCheckList(Integer status, String checkType) {
        return baseMapper.selectCheckList(status, checkType);
    }

    @Override
    public InventoryCheck getCheckWithItems(Integer id) {
        InventoryCheck check = this.getById(id);
        if (check != null) {
            List<InventoryCheckItem> items = itemMapper.selectByCheckId(id);
            check.setItems(items);
        }
        return check;
    }

    @Override
    public void createCheck(InventoryCheck check, List<InventoryCheckItem> items) {
        String checkNo = baseMapper.generateCheckNo();
        if (checkNo == null) {
            checkNo = "PD" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()) + "0001";
        }
        check.setCheckNo(checkNo);
        check.setStatus(0);
        check.setCreateTime(new Date());
        this.save(check);
        if (items != null && !items.isEmpty()) {
            for (InventoryCheckItem item : items) {
                item.setCheckId(check.getId());
            }
            itemMapper.batchInsert(items);
        }
    }

    @Override
    public void updateCheckItems(Integer checkId, List<InventoryCheckItem> items) {
        itemMapper.deleteByCheckId(checkId);
        if (items != null && !items.isEmpty()) {
            for (InventoryCheckItem item : items) {
                item.setCheckId(checkId);
                if (item.getActualNumber() != null && item.getSystemNumber() != null) {
                    item.setDifferenceNumber(item.getActualNumber() - item.getSystemNumber());
                }
            }
            itemMapper.batchInsert(items);
        }
    }

    @Override
    public void completeCheck(Integer checkId) {
        InventoryCheck check = this.getById(checkId);
        if (check != null && check.getStatus() == 0) {
            List<InventoryCheckItem> items = itemMapper.selectByCheckId(checkId);
            for (InventoryCheckItem item : items) {
                if (item.getActualNumber() != null) {
                    Goods goods = goodsMapper.selectById(item.getGoodsid());
                    if (goods != null) {
                        goods.setNumber(item.getActualNumber());
                        goodsMapper.updateById(goods);
                    }
                }
            }
            check.setStatus(1);
            check.setCheckDate(new Date());
            this.updateById(check);
        }
    }

    @Override
    public void cancelCheck(Integer checkId) {
        InventoryCheck check = this.getById(checkId);
        if (check != null && check.getStatus() == 0) {
            check.setStatus(2);
            this.updateById(check);
        }
    }

    @Override
    public List<InventoryCheckItem> getDifferenceReport(Integer checkId) {
        return itemMapper.selectByCheckId(checkId);
    }

    public List<InventoryCheckItem> generateCheckItems(Integer warehouseId, String categoryName) {
        List<InventoryCheckItem> items = new ArrayList<>();
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("available", 1);
        List<Goods> goodsList = goodsMapper.selectList(wrapper);
        for (Goods goods : goodsList) {
            InventoryCheckItem item = new InventoryCheckItem();
            item.setGoodsid(goods.getId());
            item.setSystemNumber(goods.getNumber());
            item.setActualNumber(goods.getNumber());
            item.setDifferenceNumber(0);
            items.add(item);
        }
        return items;
    }
}
