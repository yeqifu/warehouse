-- ============================================
-- 库存预警与盘点管理模块数据库脚本
-- ============================================

-- --------------------------------------------
-- 1. 商品库存阈值设置表
-- --------------------------------------------
DROP TABLE IF EXISTS `bus_stock_threshold`;
CREATE TABLE `bus_stock_threshold` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `goodsid` INT(11) NOT NULL COMMENT '商品ID',
    `minstock` INT(11) DEFAULT NULL COMMENT '最低库存阈值（低于此值触发预警）',
    `maxstock` INT(11) DEFAULT NULL COMMENT '最高库存阈值（高于此值触发预警）',
    `warningenabled` INT(1) DEFAULT 1 COMMENT '是否启用预警：0-禁用，1-启用',
    `warningmethods` VARCHAR(20) DEFAULT '1' COMMENT '预警方式：1-站内信，2-邮件，3-短信，多个用逗号分隔',
    `createtime` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updatetime` DATETIME DEFAULT NULL COMMENT '更新时间',
    `operateperson` VARCHAR(255) DEFAULT NULL COMMENT '操作人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_goodsid` (`goodsid`),
    KEY `idx_warningenabled` (`warningenabled`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='商品库存阈值设置表';

-- --------------------------------------------
-- 2. 库存预警记录表
-- --------------------------------------------
DROP TABLE IF EXISTS `bus_inventory_warning`;
CREATE TABLE `bus_inventory_warning` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `goodsid` INT(11) NOT NULL COMMENT '商品ID',
    `warningtype` INT(1) NOT NULL COMMENT '预警类型：1-库存不足预警，2-库存积压预警',
    `currentnum` INT(11) DEFAULT NULL COMMENT '当前库存数量',
    `threshold` INT(11) DEFAULT NULL COMMENT '预警阈值',
    `status` INT(1) DEFAULT 0 COMMENT '预警状态：0-未处理，1-已处理',
    `warningtime` DATETIME DEFAULT NULL COMMENT '预警时间',
    `handletime` DATETIME DEFAULT NULL COMMENT '处理时间',
    `handleperson` VARCHAR(255) DEFAULT NULL COMMENT '处理人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_goodsid` (`goodsid`),
    KEY `idx_warningtype` (`warningtype`),
    KEY `idx_status` (`status`),
    KEY `idx_warningtime` (`warningtime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警记录表';

-- --------------------------------------------
-- 3. 库存盘点单表
-- --------------------------------------------
DROP TABLE IF EXISTS `bus_inventory_check`;
CREATE TABLE `bus_inventory_check` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `checkno` VARCHAR(50) NOT NULL COMMENT '盘点单号',
    `checktype` INT(1) NOT NULL COMMENT '盘点类型：1-全盘，2-按仓库盘点，3-按品类盘点',
    `warehouseid` INT(11) DEFAULT NULL COMMENT '仓库ID（按仓库盘点时使用）',
    `categoryid` INT(11) DEFAULT NULL COMMENT '品类ID（按品类盘点时使用）',
    `status` INT(1) DEFAULT 0 COMMENT '盘点状态：0-待盘点，1-盘点中，2-已完成',
    `starttime` DATETIME DEFAULT NULL COMMENT '盘点开始时间',
    `endtime` DATETIME DEFAULT NULL COMMENT '盘点结束时间',
    `createperson` VARCHAR(255) DEFAULT NULL COMMENT '创建人',
    `checkperson` VARCHAR(255) DEFAULT NULL COMMENT '盘点人',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_checkno` (`checkno`),
    KEY `idx_checktype` (`checktype`),
    KEY `idx_status` (`status`),
    KEY `idx_starttime` (`starttime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点单表';

-- --------------------------------------------
-- 4. 库存盘点明细表
-- --------------------------------------------
DROP TABLE IF EXISTS `bus_inventory_check_detail`;
CREATE TABLE `bus_inventory_check_detail` (
    `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `checkid` INT(11) NOT NULL COMMENT '盘点单ID',
    `goodsid` INT(11) NOT NULL COMMENT '商品ID',
    `booknum` INT(11) DEFAULT 0 COMMENT '账面数量',
    `actualnum` INT(11) DEFAULT 0 COMMENT '实盘数量',
    `diffnum` INT(11) DEFAULT 0 COMMENT '差异数量（实盘-账面）',
    `diffamount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '差异金额',
    `difftype` INT(1) DEFAULT 0 COMMENT '差异类型：0-无差异，1-盘盈，2-盘亏',
    `checktime` DATETIME DEFAULT NULL COMMENT '盘点时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_checkid` (`checkid`),
    KEY `idx_goodsid` (`goodsid`),
    KEY `idx_difftype` (`difftype`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点明细表';

-- --------------------------------------------
-- 初始化数据示例
-- --------------------------------------------

-- 为现有商品添加库存阈值设置（可选）
-- INSERT INTO bus_stock_threshold (goodsid, minstock, maxstock, warningenabled, warningmethods, createtime, updatetime)
-- SELECT id, 10, 1000, 1, '1', NOW(), NOW() FROM bus_goods WHERE available = 1;
