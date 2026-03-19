package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckDetail;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InventoryCheckDetailMapper;
import com.yeqifu.bus.mapper.InventoryCheckMapper;
import com.yeqifu.bus.service.IInventoryCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 库存盘点单表 服务实现类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Service
@Transactional
public class InventoryCheckServiceImpl extends ServiceImpl<InventoryCheckMapper, InventoryCheck> implements IInventoryCheckService {

    @Autowired
    private InventoryCheckDetailMapper inventoryCheckDetailMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public InventoryCheck generateCheckOrder(InventoryCheck inventoryCheck) {
        // 生成盘点单号
        String checkno = generateCheckNo();
        inventoryCheck.setCheckno(checkno);
        inventoryCheck.setStatus(0); // 待盘点
        inventoryCheck.setStarttime(new Date());
        this.save(inventoryCheck);

        // 根据盘点类型生成盘点明细
        List<InventoryCheckDetail> details = new ArrayList<>();
        List<Goods> goodsList = new ArrayList<>();

        switch (inventoryCheck.getChecktype()) {
            case 1: // 全盘
                goodsList = goodsMapper.selectList(new QueryWrapper<Goods>());
                break;
            case 2: // 按仓库盘点
                // 假设Goods表中有warehouseid字段，这里简化处理
                goodsList = goodsMapper.selectList(new QueryWrapper<Goods>());
                break;
            case 3: // 按品类盘点
                // 假设Goods表中有categoryid字段，这里简化处理
                goodsList = goodsMapper.selectList(new QueryWrapper<Goods>());
                break;
            default:
                break;
        }

        // 生成盘点明细
        for (Goods goods : goodsList) {
            InventoryCheckDetail detail = new InventoryCheckDetail();
            detail.setCheckid(inventoryCheck.getId());
            detail.setGoodsid(goods.getId());
            detail.setBooknum(goods.getNumber()); // 账面数量
            detail.setActualnum(0); // 实盘数量初始为0
            detail.setDiffnum(0); // 差异初始为0
            detail.setDiffamount(0.0); // 差异金额初始为0
            detail.setDifftype(0); // 无差异
            details.add(detail);
        }

        // 批量保存盘点明细
        if (!details.isEmpty()) {
            inventoryCheckDetailMapper.batchInsert(details);
        }

        return inventoryCheck;
    }

    @Override
    public void startCheck(Integer id, String checkperson) {
        InventoryCheck check = this.getById(id);
        if (check != null && check.getStatus() == 0) {
            check.setStatus(1); // 盘点中
            check.setCheckperson(checkperson);
            check.setStarttime(new Date());
            this.updateById(check);
        }
    }

    @Override
    public void completeCheck(Integer id) {
        InventoryCheck check = this.getById(id);
        if (check != null && check.getStatus() == 1) {
            check.setStatus(2); // 已完成
            check.setEndtime(new Date());
            this.updateById(check);

            // 计算盘点差异
            calculateCheckDifference(id);
        }
    }

    @Override
    public void submitCheckDetails(List<InventoryCheckDetail> details) {
        for (InventoryCheckDetail detail : details) {
            // 计算差异
            Integer bookNum = detail.getBooknum();
            Integer actualNum = detail.getActualnum();
            Integer diffNum = actualNum - bookNum;
            detail.setDiffnum(diffNum);

            // 获取商品单价
            Goods goods = goodsMapper.selectById(detail.getGoodsid());
            if (goods != null) {
                Double price = goods.getPrice();
                detail.setDiffamount(diffNum * price);

                // 设置差异类型
                if (diffNum > 0) {
                    detail.setDifftype(1); // 盘盈
                } else if (diffNum < 0) {
                    detail.setDifftype(2); // 盘亏
                } else {
                    detail.setDifftype(0); // 无差异
                }
            }

            detail.setChecktime(new Date());
            inventoryCheckDetailMapper.updateById(detail);
        }
    }

    @Override
    public List<InventoryCheckDetail> generateProfitLossReport(Integer checkid) {
        // 查询盘点差异统计
        List<InventoryCheckDetail> diffList = inventoryCheckDetailMapper.selectDiffStatistics(checkid);

        // 补充商品信息
        for (InventoryCheckDetail detail : diffList) {
            Goods goods = goodsMapper.selectById(detail.getGoodsid());
            if (goods != null) {
                detail.setGoodsname(goods.getGoodsname());
                detail.setSize(goods.getSize());
                detail.setPrice(goods.getPrice());
            }
        }

        return diffList;
    }

    @Override
    public InventoryCheck generateCheckByWarehouse(Integer warehouseid, String createperson, String remark) {
        InventoryCheck check = new InventoryCheck();
        check.setChecktype(2); // 按仓库盘点
        check.setWarehouseid(warehouseid);
        check.setCreateperson(createperson);
        check.setRemark(remark);
        return this.generateCheckOrder(check);
    }

    @Override
    public InventoryCheck generateCheckByCategory(Integer categoryid, String createperson, String remark) {
        InventoryCheck check = new InventoryCheck();
        check.setChecktype(3); // 按品类盘点
        check.setCategoryid(categoryid);
        check.setCreateperson(createperson);
        check.setRemark(remark);
        return this.generateCheckOrder(check);
    }

    @Override
    public InventoryCheck generateFullCheck(String createperson, String remark) {
        InventoryCheck check = new InventoryCheck();
        check.setChecktype(1); // 全盘
        check.setCreateperson(createperson);
        check.setRemark(remark);
        return this.generateCheckOrder(check);
    }

    /**
     * 生成盘点单号
     * @return 盘点单号
     */
    private String generateCheckNo() {
        String prefix = "PD";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return prefix + dateStr + uuid;
    }

    /**
     * 计算盘点差异
     * @param checkid 盘点单ID
     */
    private void calculateCheckDifference(Integer checkid) {
        List<InventoryCheckDetail> details = inventoryCheckDetailMapper.selectByCheckid(checkid);

        for (InventoryCheckDetail detail : details) {
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

                inventoryCheckDetailMapper.updateById(detail);
            }
        }
    }

}
