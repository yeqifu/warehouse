package com.yeqifu.bus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.InventoryWarning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryWarningMapper extends BaseMapper<InventoryWarning> {

    List<InventoryWarning> selectWarningList(@Param("status") Integer status);

    List<InventoryWarning> selectUnhandledWarnings();

    int updateStatus(@Param("id") Integer id, @Param("status") Integer status, @Param("handler") String handler, @Param("handleRemark") String handleRemark);
}
