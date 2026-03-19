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
 * 库存预警记录表
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inventory_warning")
@ToString
public class InventoryWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID
     */
    private Integer goodsid;

    /**
     * 预警类型：1-库存不足预警，2-库存积压预警
     */
    private Integer warningtype;

    /**
     * 当前库存数量
     */
    private Integer currentnum;

    /**
     * 预警阈值
     */
    private Integer threshold;

    /**
     * 预警状态：0-未处理，1-已处理
     */
    private Integer status;

    /**
     * 预警时间
     */
    private Date warningtime;

    /**
     * 处理时间
     */
    private Date handletime;

    /**
     * 处理人
     */
    private String handleperson;

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
     * 供应商名称
     */
    @TableField(exist = false)
    private String providername;


}
