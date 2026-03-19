package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryCheck;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 库存盘点单VO
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryCheckVo extends InventoryCheck {

    /**
     * 分页参数
     */
    private Integer page = 1;
    private Integer limit = 10;

    /**
     * 查询条件
     */
    private String starttime;
    private String endtime;

    /**
     * 盘点类型名称
     */
    public String getChecktypeName() {
        if (getChecktype() == null) {
            return "";
        }
        switch (getChecktype()) {
            case 1:
                return "全盘";
            case 2:
                return "按仓库盘点";
            case 3:
                return "按品类盘点";
            default:
                return "";
        }
    }

    /**
     * 状态名称
     */
    public String getStatusName() {
        if (getStatus() == null) {
            return "";
        }
        switch (getStatus()) {
            case 0:
                return "待盘点";
            case 1:
                return "盘点中";
            case 2:
                return "已完成";
            default:
                return "";
        }
    }

}
