package com.yeqifu.bus.task;

import com.yeqifu.bus.service.IInventoryWarningService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class InventoryWarningTask {

    @Autowired
    private IInventoryWarningService inventoryWarningService;

    @Scheduled(cron = "0 0 8 * * ?")
    public void checkInventoryWarning() {
        log.info("开始执行库存预警检查任务...");
        try {
            inventoryWarningService.checkAndCreateWarnings();
            log.info("库存预警检查任务执行完成");
        } catch (Exception e) {
            log.error("库存预警检查任务执行失败", e);
        }
    }

    @Scheduled(cron = "0 0 9,14,18 * * ?")
    public void remindUnhandledWarnings() {
        log.info("开始执行未处理预警提醒任务...");
        try {
            int count = inventoryWarningService.getUnhandledWarnings().size();
            if (count > 0) {
                log.info("当前有" + count + "条未处理的库存预警");
            }
        } catch (Exception e) {
            log.error("未处理预警提醒任务执行失败", e);
        }
    }
}
