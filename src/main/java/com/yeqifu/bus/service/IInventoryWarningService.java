package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.InventoryWarning;

import java.util.List;

public interface IInventoryWarningService extends IService<InventoryWarning> {

    List<InventoryWarning> getWarningList(Integer status);

    List<InventoryWarning> getUnhandledWarnings();

    void checkAndCreateWarnings();

    void handleWarning(Integer id, String handler, String handleRemark);

    void sendWarningNotification(InventoryWarning warning);
}
