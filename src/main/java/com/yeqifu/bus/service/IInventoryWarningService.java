package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.InventoryWarning;

import java.util.List;

/**
 * <p>
 * 库存预警记录表 服务类
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
public interface IInventoryWarningService extends IService<InventoryWarning> {

    /**
     * 检查库存并生成预警记录
     */
    void checkInventoryAndGenerateWarnings();

    /**
     * 处理预警记录
     * @param id 预警记录ID
     * @param handleperson 处理人
     * @param remark 备注
     */
    void handleWarning(Integer id, String handleperson, String remark);

    /**
     * 批量处理预警记录
     * @param ids 预警记录ID列表
     * @param handleperson 处理人
     * @param remark 备注
     */
    void batchHandleWarnings(List<Integer> ids, String handleperson, String remark);

    /**
     * 获取未处理的预警数量
     * @return 预警数量
     */
    Integer getUnhandledWarningCount();

    /**
     * 发送预警通知（站内信/邮件/短信）
     * @param warning 预警记录
     */
    void sendWarningNotification(InventoryWarning warning);

}
