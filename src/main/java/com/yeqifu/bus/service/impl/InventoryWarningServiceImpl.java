package com.yeqifu.bus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.InventoryWarning;
import com.yeqifu.bus.entity.StockThreshold;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InventoryWarningMapper;
import com.yeqifu.bus.mapper.StockThresholdMapper;
import com.yeqifu.bus.service.IInventoryWarningService;
import com.yeqifu.sys.entity.Notice;
import com.yeqifu.sys.entity.User;
import com.yeqifu.sys.service.INoticeService;
import com.yeqifu.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 库存预警记录表 服务实现类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Service
@Transactional
public class InventoryWarningServiceImpl extends ServiceImpl<InventoryWarningMapper, InventoryWarning> implements IInventoryWarningService {

    @Autowired
    private StockThresholdMapper stockThresholdMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private INoticeService noticeService;

    @Autowired
    private IUserService userService;

    @Override
    public void checkInventoryAndGenerateWarnings() {
        // 查询所有启用了预警的阈值设置
        List<StockThreshold> thresholds = stockThresholdMapper.selectAllEnabledThresholds();

        for (StockThreshold threshold : thresholds) {
            Goods goods = goodsMapper.selectById(threshold.getGoodsid());
            if (goods == null) {
                continue;
            }

            Integer currentStock = goods.getNumber();
            Integer minStock = threshold.getMinstock();
            Integer maxStock = threshold.getMaxstock();

            // 检查库存不足
            if (minStock != null && currentStock < minStock) {
                // 检查是否已存在未处理的库存不足预警
                List<InventoryWarning> existingWarnings = baseMapper.selectUnhandledByGoodsid(
                        threshold.getGoodsid(), 1);
                if (existingWarnings.isEmpty()) {
                    // 创建新的预警记录
                    InventoryWarning warning = new InventoryWarning();
                    warning.setGoodsid(threshold.getGoodsid());
                    warning.setWarningtype(1); // 库存不足
                    warning.setCurrentnum(currentStock);
                    warning.setThreshold(minStock);
                    warning.setStatus(0); // 未处理
                    warning.setWarningtime(new Date());
                    this.save(warning);

                    // 发送通知
                    sendWarningNotification(warning);
                }
            }

            // 检查库存积压
            if (maxStock != null && currentStock > maxStock) {
                // 检查是否已存在未处理的库存积压预警
                List<InventoryWarning> existingWarnings = baseMapper.selectUnhandledByGoodsid(
                        threshold.getGoodsid(), 2);
                if (existingWarnings.isEmpty()) {
                    // 创建新的预警记录
                    InventoryWarning warning = new InventoryWarning();
                    warning.setGoodsid(threshold.getGoodsid());
                    warning.setWarningtype(2); // 库存积压
                    warning.setCurrentnum(currentStock);
                    warning.setThreshold(maxStock);
                    warning.setStatus(0); // 未处理
                    warning.setWarningtime(new Date());
                    this.save(warning);

                    // 发送通知
                    sendWarningNotification(warning);
                }
            }
        }
    }

    @Override
    public void handleWarning(Integer id, String handleperson, String remark) {
        InventoryWarning warning = this.getById(id);
        if (warning != null && warning.getStatus() == 0) {
            warning.setStatus(1); // 已处理
            warning.setHandleperson(handleperson);
            warning.setHandletime(new Date());
            warning.setRemark(remark);
            this.updateById(warning);
        }
    }

    @Override
    public void batchHandleWarnings(List<Integer> ids, String handleperson, String remark) {
        for (Integer id : ids) {
            this.handleWarning(id, handleperson, remark);
        }
    }

    @Override
    public Integer getUnhandledWarningCount() {
        return baseMapper.countUnhandledWarning();
    }

    @Override
    public void sendWarningNotification(InventoryWarning warning) {
        // 获取商品信息
        Goods goods = goodsMapper.selectById(warning.getGoodsid());
        if (goods == null) {
            return;
        }

        // 获取阈值设置
        StockThreshold threshold = stockThresholdMapper.selectByGoodsid(warning.getGoodsid());
        if (threshold == null) {
            return;
        }

        String warningTypeName = warning.getWarningtype() == 1 ? "库存不足" : "库存积压";
        String title = "库存" + warningTypeName + "预警";
        String content = String.format("商品【%s】%s预警：当前库存 %d，%s %d，请及时处理。",
                goods.getGoodsname(),
                warningTypeName,
                warning.getCurrentnum(),
                warning.getWarningtype() == 1 ? "最低库存阈值" : "最高库存阈值",
                warning.getThreshold());

        String warningMethods = threshold.getWarningmethods();
        if (warningMethods == null || warningMethods.isEmpty()) {
            warningMethods = "1"; // 默认站内信
        }

        String[] methods = warningMethods.split(",");
        for (String method : methods) {
            switch (method.trim()) {
                case "1": // 站内信
                    sendSiteMessage(title, content);
                    break;
                case "2": // 邮件
                    sendEmail(title, content);
                    break;
                case "3": // 短信
                    sendSms(title, content);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 发送站内信
     */
    private void sendSiteMessage(String title, String content) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setCreatetime(new Date());
        notice.setOpername("系统");
        noticeService.save(notice);
    }

    /**
     * 发送邮件（预留接口）
     */
    private void sendEmail(String title, String content) {
        // TODO: 实现邮件发送功能
        // 需要集成邮件服务，如JavaMailSender
        System.out.println("发送邮件通知：" + title + " - " + content);
    }

    /**
     * 发送短信（预留接口）
     */
    private void sendSms(String title, String content) {
        // TODO: 实现短信发送功能
        // 需要集成短信服务，如阿里云短信、腾讯云短信等
        System.out.println("发送短信通知：" + title + " - " + content);
    }

}
