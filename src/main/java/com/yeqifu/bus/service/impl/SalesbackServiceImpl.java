package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.entity.Salesback;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.SalesMapper;
import com.yeqifu.bus.mapper.SalesbackMapper;
import com.yeqifu.bus.service.ISalesbackService;
import com.yeqifu.sys.common.WebUtils;
import com.yeqifu.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * InnoDB free: 9216 kB 服务实现类
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-23
 */
@Service
public class SalesbackServiceImpl extends ServiceImpl<SalesbackMapper, Salesback> implements ISalesbackService {

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * @param id    销售单ID
     * @param number    退货数量
     * @param remark    备注
     */
    @Override
    public void addSalesback(Integer id, Integer number, String remark) {
        //1.通过销售单ID查询出销售单信息
        Sales sales = salesMapper.selectById(id);
        //2.根据商品ID查询商品信息
        Goods goods = goodsMapper.selectById(sales.getGoodsid());
        //3.修改商品的数量     商品的数量-退货的数量
        goods.setNumber(goods.getNumber()+number);

        //修改进货的数量
        sales.setNumber(sales.getNumber()-number);
        salesMapper.updateById(sales);

        //4.进行修改
        goodsMapper.updateById(goods);

        //5.添加退货单信息
        Salesback salesback = new Salesback();
        salesback.setGoodsid(sales.getGoodsid());

        salesback.setNumber(number);
        User user = (User) WebUtils.getSession().getAttribute("user");
        salesback.setOperateperson(user.getName());


        salesback.setSalebackprice(sales.getSaleprice());
        salesback.setPaytype(sales.getPaytype());

        salesback.setSalesbacktime(new Date());
        salesback.setRemark(remark);


        salesback.setCustomerid(sales.getCustomerid());


        getBaseMapper().insert(salesback);
    }
    
}
