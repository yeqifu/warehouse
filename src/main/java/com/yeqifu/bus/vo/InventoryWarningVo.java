package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryWarning;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 库存预警记录VO
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryWarningVo extends InventoryWarning {

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
     * 预警类型名称
     */
    public String getWarningtypeName() {
        if (getWarningtype() == null) {
            return "";
        }
        switch (getWarningtype()) {
            case 1:
                return "库存不足";
            case 2:
                return "库存积压";
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
                return "未处理";
            case 1:
                return "已处理";
            default:
                return "";
        }
    }

}
