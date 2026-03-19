package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.StockThreshold;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.StockThresholdMapper;
import com.yeqifu.bus.service.IStockThresholdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商品库存阈值设置表 服务实现类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Service
@Transactional
public class StockThresholdServiceImpl extends ServiceImpl<StockThresholdMapper, StockThreshold> implements IStockThresholdService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public StockThreshold getByGoodsid(Integer goodsid) {
        return baseMapper.selectByGoodsid(goodsid);
    }

    @Override
    public void saveOrUpdateThreshold(StockThreshold stockThreshold) {
        // 检查是否已存在该商品的阈值设置
        StockThreshold existing = baseMapper.selectByGoodsid(stockThreshold.getGoodsid());
        if (existing != null) {
            // 更新
            stockThreshold.setId(existing.getId());
            stockThreshold.setUpdatetime(new Date());
            this.updateById(stockThreshold);
        } else {
            // 新增
            stockThreshold.setCreatetime(new Date());
            stockThreshold.setUpdatetime(new Date());
            this.save(stockThreshold);
        }
    }

    @Override
    public void batchSetThreshold(List<Integer> goodsids, Integer minstock, Integer maxstock,
                                  Integer warningenabled, String warningmethods, String operateperson) {
        for (Integer goodsid : goodsids) {
            StockThreshold threshold = new StockThreshold();
            threshold.setGoodsid(goodsid);
            threshold.setMinstock(minstock);
            threshold.setMaxstock(maxstock);
            threshold.setWarningenabled(warningenabled);
            threshold.setWarningmethods(warningmethods);
            threshold.setOperateperson(operateperson);
            this.saveOrUpdateThreshold(threshold);
        }
    }

    @Override
    public List<StockThreshold> getWarningGoods() {
        return baseMapper.selectWarningGoods();
    }

    @Override
    public Integer checkStockWarning(Integer goodsid) {
        Goods goods = goodsMapper.selectById(goodsid);
        if (goods == null) {
            return 0; // 商品不存在，返回正常
        }

        StockThreshold threshold = baseMapper.selectByGoodsid(goodsid);
        if (threshold == null || threshold.getWarningenabled() == 0) {
            return 0; // 未设置阈值或未启用预警
        }

        Integer currentStock = goods.getNumber();
        Integer minStock = threshold.getMinstock();
        Integer maxStock = threshold.getMaxstock();

        if (minStock != null && currentStock < minStock) {
            return 1; // 库存不足
        }

        if (maxStock != null && currentStock > maxStock) {
            return 2; // 库存积压
        }

        return 0; // 正常
    }

}
