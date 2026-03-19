package com.yeqifu.bus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inventory_check_item")
public class InventoryCheckItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer checkId;

    private Integer goodsid;

    private Integer systemNumber;

    private Integer actualNumber;

    private Integer differenceNumber;

    private String remark;

    @TableField(exist = false)
    private String goodsname;

    @TableField(exist = false)
    private String productcode;
}
