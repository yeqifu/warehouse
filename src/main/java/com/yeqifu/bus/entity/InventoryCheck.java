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
 * 库存盘点单表
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inventory_check")
@ToString
public class InventoryCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 盘点单号
     */
    private String checkno;

    /**
     * 盘点类型：1-全盘，2-按仓库盘点，3-按品类盘点
     */
    private Integer checktype;

    /**
     * 仓库ID（按仓库盘点时使用）
     */
    private Integer warehouseid;

    /**
     * 品类ID（按品类盘点时使用）
     */
    private Integer categoryid;

    /**
     * 盘点状态：0-待盘点，1-盘点中，2-已完成
     */
    private Integer status;

    /**
     * 盘点开始时间
     */
    private Date starttime;

    /**
     * 盘点结束时间
     */
    private Date endtime;

    /**
     * 创建人
     */
    private String createperson;

    /**
     * 盘点人
     */
    private String checkperson;

    /**
     * 备注
     */
    private String remark;

    /**
     * 仓库名称
     */
    @TableField(exist = false)
    private String warehousename;

    /**
     * 品类名称
     */
    @TableField(exist = false)
    private String categoryname;


}
