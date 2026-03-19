package com.yeqifu.bus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inventory_warning")
public class InventoryWarning implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer goodsid;

    private Integer type;

    private Integer warningLevel;

    private String content;

    private Integer status;

    private Date createTime;

    private Date handleTime;

    private String handler;

    private String handleRemark;

    @TableField(exist = false)
    private String goodsname;

    @TableField(exist = false)
    private Integer currentNumber;

    @TableField(exist = false)
    private Integer thresholdValue;
}
