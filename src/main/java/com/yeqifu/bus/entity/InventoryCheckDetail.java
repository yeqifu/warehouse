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
import java.util.Date;

/**
 * <p>
 * 库存盘点明细表
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inventory_check_detail")
@ToString
public class InventoryCheckDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点单ID
     */
    private Integer checkid;

    /**
     * 商品ID
     */
    private Integer goodsid;

    /**
     * 账面数量
     */
    private Integer booknum;

    /**
     * 实盘数量
     */
    private Integer actualnum;

    /**
     * 差异数量（实盘-账面）
     */
    private Integer diffnum;

    /**
     * 差异金额
     */
    private Double diffamount;

    /**
     * 差异类型：0-无差异，1-盘盈，2-盘亏
     */
    private Integer difftype;

    /**
     * 盘点时间
     */
    private Date checktime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 商品名称
     */
    @TableField(exist = false)
    private String goodsname;

    /**
     * 商品规格
     */
    @TableField(exist = false)
    private String size;

    /**
     * 商品单价
     */
    @TableField(exist = false)
    private Double price;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String providername;


}
