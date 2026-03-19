package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryWarning;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InventoryWarningMapper;
import com.yeqifu.bus.service.IInventoryWarningService;
import com.yeqifu.sys.entity.Notice;
import com.yeqifu.sys.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InventoryWarningServiceImpl extends ServiceImpl<InventoryWarningMapper, InventoryWarning> implements IInventoryWarningService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private INoticeService noticeService;

    @Override
    public List<InventoryWarning> getWarningList(Integer status) {
        return baseMapper.selectWarningList(status);
    }

    @Override
    public List<InventoryWarning> getUnhandledWarnings() {
        return baseMapper.selectUnhandledWarnings();
    }

    @Override
    public void checkAndCreateWarnings() {
        List<Goods> allGoods = goodsMapper.selectList(null);
        for (Goods goods : allGoods) {
            if (goods.getDangernum() != null && goods.getNumber() < goods.getDangernum()) {
                InventoryWarning warning = new InventoryWarning();
                warning.setGoodsid(goods.getId());
                warning.setType(1);
                warning.setWarningLevel(goods.getNumber() < goods.getDangernum() / 2 ? 2 : 1);
                warning.setContent("商品【" + goods.getGoodsname() + "】库存不足，当前库存：" + goods.getNumber() + "，最低库存：" + goods.getDangernum());
                warning.setStatus(0);
                warning.setCreateTime(new Date());
                this.save(warning);
                sendWarningNotification(warning);
            }
            if (goods.getMaxnum() != null && goods.getNumber() > goods.getMaxnum()) {
                InventoryWarning warning = new InventoryWarning();
                warning.setGoodsid(goods.getId());
                warning.setType(2);
                warning.setWarningLevel(goods.getNumber() > goods.getMaxnum() * 1.5 ? 2 : 1);
                warning.setContent("商品【" + goods.getGoodsname() + "】库存过剩，当前库存：" + goods.getNumber() + "，最高库存：" + goods.getMaxnum());
                warning.setStatus(0);
                warning.setCreateTime(new Date());
                this.save(warning);
                sendWarningNotification(warning);
            }
        }
    }

    @Override
    public void handleWarning(Integer id, String handler, String handleRemark) {
        baseMapper.updateStatus(id, 1, handler, handleRemark);
    }

    @Override
    public void sendWarningNotification(InventoryWarning warning) {
        Notice notice = new Notice();
        notice.setTitle("库存预警通知");
        notice.setContent(warning.getContent());
        notice.setCreatetime(new Date());
        notice.setOpername("系统");
        noticeService.save(notice);
    }
}
