package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryCheck;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryCheckVo extends InventoryCheck {

    private Integer page = 1;

    private Integer limit = 10;
}
