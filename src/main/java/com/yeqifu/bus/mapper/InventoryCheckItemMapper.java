package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryCheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryCheckItemMapper extends BaseMapper<InventoryCheckItem> {

    List<InventoryCheckItem> selectByCheckId(@Param("checkId") Integer checkId);

    void deleteByCheckId(@Param("checkId") Integer checkId);

    void batchInsert(@Param("items") List<InventoryCheckItem> items);
}
