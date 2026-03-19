package com.yeqifu.bus.task;

import com.yeqifu.bus.service.IInventoryWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 库存预警定时任务
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Component
@EnableScheduling
public class InventoryWarningTask {

    @Autowired
    private IInventoryWarningService inventoryWarningService;

    /**
     * 定时检查库存预警
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void checkInventoryWarning() {
        System.out.println("========== 开始执行库存预警检查任务 ==========");
        try {
            inventoryWarningService.checkInventoryAndGenerateWarnings();
            System.out.println("========== 库存预警检查任务执行完成 ==========");
        } catch (Exception e) {
            System.err.println("========== 库存预警检查任务执行失败 ==========");
            e.printStackTrace();
        }
    }

    /**
     * 每天早上8点发送预警汇总
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyWarningSummary() {
        System.out.println("========== 开始发送每日预警汇总 ==========");
        try {
            Integer count = inventoryWarningService.getUnhandledWarningCount();
            if (count > 0) {
                System.out.println("当前有 " + count + " 条未处理的库存预警记录");
                // TODO: 可以在这里发送邮件或短信汇总通知给管理员
            }
            System.out.println("========== 每日预警汇总发送完成 ==========");
        } catch (Exception e) {
            System.err.println("========== 每日预警汇总发送失败 ==========");
            e.printStackTrace();
        }
    }

}
