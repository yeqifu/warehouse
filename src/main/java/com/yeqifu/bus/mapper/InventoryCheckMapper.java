package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryCheckMapper extends BaseMapper<InventoryCheck> {

    List<InventoryCheck> selectCheckList(@Param("status") Integer status, @Param("checkType") String checkType);

    String generateCheckNo();
}
