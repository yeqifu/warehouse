package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryWarning;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryWarningVo extends InventoryWarning {

    private Integer page = 1;

    private Integer limit = 10;
}
