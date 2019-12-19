package com.yeqifu.bus.service.impl;

import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Inport;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InportMapper;
import com.yeqifu.bus.service.IInportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
@Service
@Transactional
public class InportServiceImpl extends ServiceImpl<InportMapper, Inport> implements IInportService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 保存商品进货
     * @param entity
     * @return
     */
    @Override
    public boolean save(Inport entity) {
        //根据商品ID查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setNumber(goods.getNumber()+entity.getNumber());
        goodsMapper.updateById(goods);
        //保存进货信息
        return super.save(entity);
    }

    /**
     * 更新商品进货
     * @param entity
     * @return
     */
    @Override
    public boolean updateById(Inport entity) {
        //根据进货ID查询进货信息
        Inport inport = baseMapper.selectById(entity.getId());
        //根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        //库存算法  当前库存-进货单修改之前的数量+修改之后的数量
        goods.setNumber(goods.getNumber()-inport.getNumber()+entity.getNumber());
        goodsMapper.updateById(goods);
        //更新进货单
        return super.updateById(entity);
    }

    /**
     * 删除商品进货信息
     * @param id
     * @return
     */
    @Override
    public boolean removeById(Serializable id) {
        //根据进货ID查询进货信息
        Inport inport = baseMapper.selectById(id);
        //根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(inport.getGoodsid());
        //库存算法  当前库存-进货单数量
        goods.setNumber(goods.getNumber()-inport.getNumber());
        goodsMapper.updateById(goods);
        //更新商品的数量
        return super.removeById(id);
    }
}
