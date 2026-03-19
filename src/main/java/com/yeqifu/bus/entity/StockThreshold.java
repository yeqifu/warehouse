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
 * 商品库存阈值设置表
 * </p>
 *
 * @author inventory-system
 * @since 2026-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_stock_threshold")
@ToString
public class StockThreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品ID
     */
    private Integer goodsid;

    /**
     * 最低库存阈值（低于此值触发预警）
     */
    private Integer minstock;

    /**
     * 最高库存阈值（高于此值触发预警）
     */
    private Integer maxstock;

    /**
     * 是否启用预警：0-禁用，1-启用
     */
    private Integer warningenabled;

    /**
     * 预警方式：1-站内信，2-邮件，3-短信，多个用逗号分隔
     */
    private String warningmethods;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;

    /**
     * 操作人
     */
    private String operateperson;

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
     * 当前库存
     */
    @TableField(exist = false)
    private Integer currentstock;

    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String providername;


}
