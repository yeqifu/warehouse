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

/**
 * <p>
 * InnoDB free: 9216 kB; (`providerid`) REFER `warehouse/bus_provider`(`id`); (`goo
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bus_inport")
public class Inport implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String paytype;

    private Date inporttime;

    private String operateperson;

    private Integer number;

    private String remark;

    private Double inportprice;

    private Integer providerid;

    private Integer goodsid;

    /**
     * 供应商姓名
     */
    @TableField(exist = false)
    private String providername;

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

}
