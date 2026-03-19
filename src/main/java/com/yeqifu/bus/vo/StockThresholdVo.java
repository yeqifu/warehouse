package com.yeqifu.bus.vo;

import com.yeqifu.bus.entity.StockThreshold;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品库存阈值设置VO
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StockThresholdVo extends StockThreshold {

    /**
     * 分页参数
     */
    private Integer page = 1;
    private Integer limit = 10;

    /**
     * 查询条件
     */
    private String goodsname;

    /**
     * 预警状态名称
     */
    public String getWarningenabledName() {
        if (getWarningenabled() == null) {
            return "";
        }
        switch (getWarningenabled()) {
            case 0:
                return "禁用";
            case 1:
                return "启用";
            default:
                return "";
        }
    }

}
