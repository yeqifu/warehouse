package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.Warehouse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: yeqifu
 * @Date: 2023/5/10 14:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseVo extends Warehouse {

    /**
     * 分页参数，当前是第一页，每页10条数据
     */
    private Integer page = 1;
    private Integer limit = 10;

    /**
     * 批量删除仓库，存放仓库ID的数组
     */
    private Integer[] ids;

}