package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryBalance;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: yeqifu
 * @Date: 2023/5/10 14:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryBalanceVo extends InventoryBalance {

    /**
     * 分页参数，当前是第一页，每页10条数据
     */
    private Integer page = 1;
    private Integer limit = 10;

    /**
     * 批量删除库存，存放库存ID的数组
     */
    private Long[] ids;

    // Additional query parameters
    private String goodsName;
    private String warehouseName;
    private String providerName;

}