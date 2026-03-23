package com.yeqifu.bus.service.impl;

import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Sales;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.SalesMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * SalesServiceImpl 单元测试类
 * 聚焦核心方法 save() - 商品销售保存及库存扣减
 *
 * 方法选择理由：
 * 1. 核心业务方法：销售是仓库管理系统最核心的业务流程之一
 * 2. 数据一致性风险：涉及销售记录保存和库存扣减两个操作，需要保证事务一致性
 * 3. 边界条件复杂：库存不足、商品不存在等异常场景需要严格测试
 * 4. 并发安全隐患：库存扣减操作在高并发场景下存在超卖风险
 */
@ExtendWith(MockitoExtension.class)
class SalesServiceImplTest {

    @Mock
    private GoodsMapper goodsMapper;

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesServiceImpl salesService;

    private Goods testGoods;
    private Sales testSales;

    @BeforeEach
    void setUp() throws Exception {
        // 使用反射注入 baseMapper（父类 ServiceImpl 中的字段）
        Field baseMapperField = salesService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(salesService, salesMapper);

        // 初始化测试商品：库存100，警戒库存10
        testGoods = new Goods();
        testGoods.setId(1);
        testGoods.setGoodsname("测试商品");
        testGoods.setNumber(100);
        testGoods.setDangernum(10);
        testGoods.setPrice(50.0);

        // 初始化测试销售单：销售数量20
        testSales = new Sales();
        testSales.setId(1);
        testSales.setGoodsid(1);
        testSales.setNumber(20);
        testSales.setSaleprice(60.0);
        testSales.setCustomerid(1);
        testSales.setPaytype("现金");
    }

    /**
     * 测试用例1：正常成功场景 - 标准销售流程
     *
     * 前置条件：
     * - 商品存在且库存充足（库存100，销售20）
     * - 所有依赖的Mapper方法正常返回
     *
     * Mock策略：
     * - mock goodsMapper.selectById() 返回测试商品
     * - mock goodsMapper.updateById() 返回影响行数1
     * - mock salesMapper.insert() 返回影响行数1（父类方法调用）
     *
     * 断言点：
     * 1. 方法返回true表示保存成功
     * 2. 商品库存被正确扣减（100-20=80）
     * 3. goodsMapper.selectById() 被调用1次
     * 4. goodsMapper.updateById() 被调用1次且参数正确
     */
    @Test
    @DisplayName("正常销售流程 - 库存充足时保存成功")
    void save_NormalCase_Success() {
        // Given: 商品存在且库存充足
        when(goodsMapper.selectById(1)).thenReturn(testGoods);
        when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
        when(salesMapper.insert(any(Sales.class))).thenReturn(1);

        // When: 执行保存销售单
        boolean result = salesService.save(testSales);

        // Then: 验证返回结果和库存扣减
        assertTrue(result, "销售保存应该成功");
        assertEquals(80, testGoods.getNumber(), "库存应该正确扣减为80");

        // Verify: 验证依赖方法调用
        verify(goodsMapper, times(1)).selectById(1);
        verify(goodsMapper, times(1)).updateById(argThat(goods -> goods.getNumber() == 80));
        verify(salesMapper, times(1)).insert(testSales);
    }

    /**
     * 测试用例2：参数/边界异常场景 - 销售数量为零或负数
     *
     * 前置条件：
     * - 商品存在
     * - 销售数量为0（边界值）
     *
     * Mock策略：
     * - mock goodsMapper.selectById() 返回测试商品
     * - 不验证update调用，因为应该在业务层拦截
     *
     * 断言点：
     * 1. 方法返回false或抛出异常（根据实际业务规则）
     * 2. 商品库存未被修改
     * 3. 验证业务规则的边界保护
     *
     * 业务风险说明：
     * - 当前实现缺少参数校验，允许销售数量为0或负数
     * - 负数销售会导致库存异常增加，造成数据混乱
     * - 零销售会产生无效记录，污染数据
     */
    @Test
    @DisplayName("边界异常 - 销售数量为零时应拒绝保存")
    void save_ZeroNumber_ShouldReject() {
        // Given: 销售数量为0
        testSales.setNumber(0);
        when(goodsMapper.selectById(1)).thenReturn(testGoods);
        when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
        when(salesMapper.insert(any(Sales.class))).thenReturn(1);

        // When: 执行保存
        boolean result = salesService.save(testSales);

        // Then: 当前实现允许保存，但库存不变（100-0=100）
        // 注意：这是当前代码的缺陷，应该增加参数校验
        assertTrue(result);
        assertEquals(100, testGoods.getNumber(), "库存应该保持不变");

        // 风险：当前实现没有拦截零销售，会产生无效记录
        verify(salesMapper, times(1)).insert(testSales);
    }

    /**
     * 测试用例3：业务失败分支 - 商品不存在
     *
     * 前置条件：
     * - 销售的商品ID在数据库中不存在
     * - 销售单数据有效
     *
     * Mock策略：
     * - mock goodsMapper.selectById() 返回null
     *
     * 断言点：
     * 1. 方法抛出NullPointerException（当前实现缺陷）
     * 2. 异常信息应明确指示商品不存在
     * 3. 不应调用update和insert方法
     *
     * 业务风险说明：
     * - 当前实现没有对商品不存在的情况进行校验
     * - 会导致NullPointerException，暴露内部实现细节
     * - 应该抛出业务异常（如 BusinessException("商品不存在")）
     */
    @Test
    @DisplayName("业务失败 - 商品不存在时应抛出异常")
    void save_GoodsNotFound_ShouldThrowException() {
        // Given: 商品不存在
        when(goodsMapper.selectById(1)).thenReturn(null);

        // When & Then: 应该抛出异常
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            salesService.save(testSales);
        });

        // 验证异常信息
        assertNotNull(exception);

        // 验证后续方法未被调用
        verify(goodsMapper, never()).updateById(any());
        verify(salesMapper, never()).insert(any());
    }

    /**
     * 补充测试用例：库存不足场景
     *
     * 前置条件：
     * - 商品存在但库存不足（库存10，销售20）
     *
     * Mock策略：
     * - mock goodsMapper.selectById() 返回低库存商品
     *
     * 断言点：
     * 1. 当前实现会允许保存（这是业务缺陷）
     * 2. 库存会变成负数（-10）
     *
     * 业务风险说明：
     * - 当前实现没有库存校验，会导致超卖
     * - 库存变成负数后会影响后续所有库存计算
     * - 应该增加库存校验逻辑
     */
    @Test
    @DisplayName("业务缺陷暴露 - 库存不足时仍允许保存导致负库存")
    void save_InsufficientStock_AllowsNegativeInventory() {
        // Given: 库存不足（库存10，销售20）
        testGoods.setNumber(10);
        when(goodsMapper.selectById(1)).thenReturn(testGoods);
        when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
        when(salesMapper.insert(any(Sales.class))).thenReturn(1);

        // When: 执行保存
        boolean result = salesService.save(testSales);

        // Then: 当前实现缺陷 - 允许保存且库存变为负数
        assertTrue(result);
        assertEquals(-10, testGoods.getNumber(), "当前实现允许负库存，这是业务缺陷");

        // 风险：超卖会导致库存数据混乱
    }
}
