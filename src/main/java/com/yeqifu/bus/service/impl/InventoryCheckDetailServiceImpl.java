package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryCheckDetail;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InventoryCheckDetailMapper;
import com.yeqifu.bus.service.IInventoryCheckDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 库存盘点明细表 服务实现类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Service
@Transactional
public class InventoryCheckDetailServiceImpl extends ServiceImpl<InventoryCheckDetailMapper, InventoryCheckDetail> implements IInventoryCheckDetailService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public List<InventoryCheckDetail> getDetailsByCheckid(Integer checkid) {
        List<InventoryCheckDetail> details = baseMapper.selectByCheckid(checkid);

        // 补充商品信息
        for (InventoryCheckDetail detail : details) {
            Goods goods = goodsMapper.selectById(detail.getGoodsid());
            if (goods != null) {
                detail.setGoodsname(goods.getGoodsname());
                detail.setSize(goods.getSize());
                detail.setPrice(goods.getPrice());
            }
        }

        return details;
    }

    @Override
    public void batchSaveDetails(List<InventoryCheckDetail> details) {
        if (details != null && !details.isEmpty()) {
            baseMapper.batchInsert(details);
        }
    }

    @Override
    public InventoryCheckDetail calculateDifference(InventoryCheckDetail detail) {
        Integer bookNum = detail.getBooknum();
        Integer actualNum = detail.getActualnum();
        Integer diffNum = actualNum - bookNum;
        detail.setDiffnum(diffNum);

        Goods goods = goodsMapper.selectById(detail.getGoodsid());
        if (goods != null) {
            Double price = goods.getPrice();
            detail.setDiffamount(diffNum * price);

            if (diffNum > 0) {
                detail.setDifftype(1); // 盘盈
            } else if (diffNum < 0) {
                detail.setDifftype(2); // 盘亏
            } else {
                detail.setDifftype(0); // 无差异
            }
        }

        detail.setChecktime(new Date());
        return detail;
    }

    @Override
    public List<InventoryCheckDetail> getDiffStatistics(Integer checkid) {
        return baseMapper.selectDiffStatistics(checkid);
    }

}
