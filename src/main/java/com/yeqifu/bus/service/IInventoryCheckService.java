package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yeqifu.bus.entity.InventoryCheck;
import com.yeqifu.bus.entity.InventoryCheckItem;

import java.util.List;

public interface IInventoryCheckService extends IService<InventoryCheck> {

    List<InventoryCheck> getCheckList(Integer status, String checkType);

    InventoryCheck getCheckWithItems(Integer id);

    void createCheck(InventoryCheck check, List<InventoryCheckItem> items);

    void updateCheckItems(Integer checkId, List<InventoryCheckItem> items);

    void completeCheck(Integer checkId);

    void cancelCheck(Integer checkId);

    List<InventoryCheckItem> getDifferenceReport(Integer checkId);
}
