-- 为商品表添加最高库存字段
ALTER TABLE bus_goods ADD COLUMN maxnum INT DEFAULT NULL COMMENT '最高库存上限' AFTER dangernum;

-- 库存预警记录表
CREATE TABLE IF NOT EXISTS bus_inventory_warning (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    goodsid INT NOT NULL COMMENT '商品ID',
    type INT NOT NULL COMMENT '预警类型：1-库存不足，2-库存过剩',
    warning_level INT DEFAULT 1 COMMENT '预警级别：1-一般，2-严重',
    content VARCHAR(500) NOT NULL COMMENT '预警内容',
    status INT DEFAULT 0 COMMENT '处理状态：0-未处理，1-已处理',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
    handler VARCHAR(50) DEFAULT NULL COMMENT '处理人',
    handle_remark VARCHAR(500) DEFAULT NULL COMMENT '处理备注',
    INDEX idx_goodsid (goodsid),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警记录表';

-- 盘点单主表
CREATE TABLE IF NOT EXISTS bus_inventory_check (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    check_no VARCHAR(50) NOT NULL COMMENT '盘点单号',
    check_type VARCHAR(20) DEFAULT 'ALL' COMMENT '盘点类型：ALL-全部，WAREHOUSE-按仓库，CATEGORY-按品类',
    warehouse_id INT DEFAULT NULL COMMENT '仓库ID（按仓库盘点时使用）',
    category_name VARCHAR(100) DEFAULT NULL COMMENT '品类名称（按品类盘点时使用）',
    check_date DATETIME DEFAULT NULL COMMENT '盘点日期',
    status INT DEFAULT 0 COMMENT '状态：0-进行中，1-已完成，2-已取消',
    operator VARCHAR(50) DEFAULT NULL COMMENT '操作人',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    UNIQUE INDEX uk_check_no (check_no),
    INDEX idx_status (status),
    INDEX idx_check_date (check_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点单主表';

-- 盘点明细表
CREATE TABLE IF NOT EXISTS bus_inventory_check_item (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    check_id INT NOT NULL COMMENT '盘点单ID',
    goodsid INT NOT NULL COMMENT '商品ID',
    system_number INT DEFAULT 0 COMMENT '系统库存数量',
    actual_number INT DEFAULT 0 COMMENT '实际盘点数量',
    difference_number INT DEFAULT 0 COMMENT '差异数量（实际-系统）',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    INDEX idx_check_id (check_id),
    INDEX idx_goodsid (goodsid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='盘点明细表';

-- 盘盈盘亏视图
CREATE OR REPLACE VIEW v_inventory_difference AS
SELECT 
    c.id AS check_id,
    c.check_no,
    c.check_date,
    c.operator,
    i.goodsid,
    g.goodsname,
    g.productcode,
    i.system_number,
    i.actual_number,
    i.difference_number,
    CASE 
        WHEN i.difference_number > 0 THEN '盘盈'
        WHEN i.difference_number < 0 THEN '盘亏'
        ELSE '无差异'
    END AS difference_type,
    ABS(i.difference_number) AS difference_abs
FROM bus_inventory_check c
JOIN bus_inventory_check_item i ON c.id = i.check_id
JOIN bus_goods g ON i.goodsid = g.id
WHERE c.status = 1 AND i.difference_number != 0;
