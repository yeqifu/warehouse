package com.yeqifu.bus.service.impl;

import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Inport;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InportMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.Serializable;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InportServiceImpl 进货服务测试")
class InportServiceImplTest {

    @Mock
    private InportMapper inportMapper;

    @Mock
    private GoodsMapper goodsMapper;

    private InportServiceImpl inportService;

    private Goods testGoods;
    private Inport testInport;

    @BeforeEach
    void setUp() {
        inportService = new InportServiceImpl();
        ReflectionTestUtils.setField(inportService, "baseMapper", inportMapper);
        ReflectionTestUtils.setField(inportService, "goodsMapper", goodsMapper);

        testGoods = new Goods();
        testGoods.setId(1);
        testGoods.setGoodsname("测试商品");
        testGoods.setNumber(100);
        testGoods.setPrice(50.0);
        testGoods.setProviderid(10);

        testInport = new Inport();
        testInport.setId(1);
        testInport.setGoodsid(1);
        testInport.setNumber(20);
        testInport.setInportprice(45.0);
        testInport.setInporttime(new Date());
        testInport.setOperateperson("张三");
        testInport.setProviderid(10);
        testInport.setPaytype("现金");
    }

    @Nested
    @DisplayName("save() 进货保存测试")
    class SaveTests {

        @Test
        @DisplayName("正常成功 - 进货保存应正确增加商品库存")
        void save_Success_ShouldIncreaseGoodsNumber() {
            when(goodsMapper.selectById(1)).thenReturn(testGoods);
            when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
            when(inportMapper.insert(any(Inport.class))).thenReturn(1);

            boolean result = inportService.save(testInport);

            assertTrue(result, "保存进货单应返回true");

            ArgumentCaptor<Goods> goodsCaptor = ArgumentCaptor.forClass(Goods.class);
            verify(goodsMapper).updateById(goodsCaptor.capture());
            assertEquals(120, goodsCaptor.getValue().getNumber(), "商品库存应为原库存100+进货数量20=120");

            verify(inportMapper).insert(testInport);
            verify(goodsMapper).selectById(1);
        }

        @Test
        @DisplayName("边界异常 - 进货数量为null时应抛出NullPointerException")
        void save_NullNumber_ShouldThrowException() {
            testInport.setNumber(null);
            when(goodsMapper.selectById(1)).thenReturn(testGoods);

            assertThrows(NullPointerException.class, () -> {
                inportService.save(testInport);
            }, "进货数量为null时应抛出NullPointerException");

            verify(goodsMapper).selectById(1);
            verify(goodsMapper, never()).updateById(any());
            verify(inportMapper, never()).insert(any());
        }

        @Test
        @DisplayName("业务失败 - 商品不存在时更新库存应失败")
        void save_GoodsNotFound_ShouldHandleGracefully() {
            when(goodsMapper.selectById(1)).thenReturn(null);

            assertThrows(NullPointerException.class, () -> {
                inportService.save(testInport);
            }, "商品不存在时应抛出NullPointerException");

            verify(goodsMapper).selectById(1);
            verify(goodsMapper, never()).updateById(any());
        }
    }

    @Nested
    @DisplayName("updateById() 进货更新测试")
    class UpdateTests {

        @Test
        @DisplayName("正常成功 - 更新进货数量应正确调整库存")
        void updateById_Success_ShouldAdjustGoodsNumber() {
            Inport existingInport = new Inport();
            existingInport.setId(1);
            existingInport.setGoodsid(1);
            existingInport.setNumber(20);

            Inport updatedInport = new Inport();
            updatedInport.setId(1);
            updatedInport.setGoodsid(1);
            updatedInport.setNumber(30);

            when(inportMapper.selectById(1)).thenReturn(existingInport);
            when(goodsMapper.selectById(1)).thenReturn(testGoods);
            when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
            when(inportMapper.updateById(any(Inport.class))).thenReturn(1);

            boolean result = inportService.updateById(updatedInport);

            assertTrue(result, "更新进货单应返回true");

            ArgumentCaptor<Goods> goodsCaptor = ArgumentCaptor.forClass(Goods.class);
            verify(goodsMapper).updateById(goodsCaptor.capture());
            assertEquals(110, goodsCaptor.getValue().getNumber(), 
                "库存计算: 100 - 20(原进货) + 30(新进货) = 110");

            verify(inportMapper).updateById(updatedInport);
        }
    }

    @Nested
    @DisplayName("removeById() 进货删除测试")
    class RemoveTests {

        @Test
        @DisplayName("正常成功 - 删除进货应正确扣减库存")
        void removeById_Success_ShouldDecreaseGoodsNumber() {
            when(inportMapper.selectById(1)).thenReturn(testInport);
            when(goodsMapper.selectById(1)).thenReturn(testGoods);
            when(goodsMapper.updateById(any(Goods.class))).thenReturn(1);
            when(inportMapper.deleteById(any(Serializable.class))).thenReturn(1);

            boolean result = inportService.removeById(1);

            assertTrue(result, "删除进货单应返回true");

            ArgumentCaptor<Goods> goodsCaptor = ArgumentCaptor.forClass(Goods.class);
            verify(goodsMapper).updateById(goodsCaptor.capture());
            assertEquals(80, goodsCaptor.getValue().getNumber(), "商品库存应为原库存100-进货数量20=80");

            verify(inportMapper).deleteById(1);
        }

        @Test
        @DisplayName("业务失败 - 删除不存在的进货单应抛出异常")
        void removeById_InportNotFound_ShouldThrowException() {
            when(inportMapper.selectById(999)).thenReturn(null);

            assertThrows(NullPointerException.class, () -> {
                inportService.removeById(999);
            }, "进货单不存在时应抛出NullPointerException");

            verify(inportMapper).selectById(999);
            verify(goodsMapper, never()).selectById(any());
            verify(inportMapper, never()).deleteById(any());
        }
    }
}
