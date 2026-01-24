package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.SalesMapper;
import com.yeqifu.bus.service.ISalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-21
 */
@Service
public class SalesServiceImpl extends ServiceImpl<SalesMapper, Sales> implements ISalesService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 添加商品销售
     * @param entity    商品销售实体类
     * @return
     */
    @Override
    public boolean save(Sales entity) {
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setNumber(goods.getNumber()-entity.getNumber());
        //更新商品的库存信息
        goodsMapper.updateById(goods);
        return super.save(entity);
    }

    /**
     * 更新商品销售
     * @param entity    商品销售实体类
     * @return
     */
    @Override
    public boolean updateById(Sales entity) {
        //根据销售单ID查询销售单信息
        Sales sales = baseMapper.selectById(entity.getId());
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        //仓库商品数量=原库存-销售单修改之前的数量+修改之后的数量
        //     80  +40 -  50     30
        goods.setNumber(goods.getNumber()+sales.getNumber()-entity.getNumber());
        //更新商品
        goodsMapper.updateById(goods);
        return super.updateById(entity);
    }

    /**
     * 删除商品销售
     * @param id    商品销售ID
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        //根据销售单ID查询销售单信息
        Sales sales = baseMapper.selectById(id);
        //根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(sales.getGoodsid());
        //将商品库存数量增加
        goods.setNumber(goods.getNumber()+sales.getNumber());
        //更新商品库存信息
        goodsMapper.updateById(goods);
        return super.removeById(id);
    }

    @Override
    public Double getTotalSales() {
        return baseMapper.getTotalSales();
    }

    @Override
    public Integer getSalesCount() {
        return baseMapper.getSalesCount();
    }

    @Override
    public List<Map<String, Object>> getSalesByMonth() {
        return baseMapper.getSalesByMonth();
    }

    @Override
    public List<Map<String, Object>> getSalesByCategory() {
        return baseMapper.getSalesByCategory();
    }

    @Override
    public List<Map<String, Object>> getSalesByCustomer() {
        return baseMapper.getSalesByCustomer();
    }

}
