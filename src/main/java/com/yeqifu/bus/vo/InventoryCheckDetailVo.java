package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.InventoryCheckDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 库存盘点明细VO
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InventoryCheckDetailVo extends InventoryCheckDetail {

    /**
     * 分页参数
     */
    private Integer page = 1;
    private Integer limit = 10;

    /**
     * 差异类型名称
     */
    public String getDifftypeName() {
        if (getDifftype() == null) {
            return "";
        }
        switch (getDifftype()) {
            case 0:
                return "无差异";
            case 1:
                return "盘盈";
            case 2:
                return "盘亏";
            default:
                return "";
        }
    }

}
