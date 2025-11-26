package com.yeqifu.bus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品库存现存量表
 * </p>
 *
 * @author yeqifu
 * @since 2023-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wms_inventory_balance")
@ToString
public class InventoryBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer warehouseId;

    private Integer goodsId;

    private String batchNo;

    private String locCode;

    private String uom;

    private BigDecimal quantityOnHand;

    private BigDecimal quantityReserved;

    @TableField(exist = false)
    private BigDecimal quantityAvailable;

    private BigDecimal safetyStock;

    private Integer status;

    private String remark;

    private Date lastCountedAt;

    private Date updatedAt;

}