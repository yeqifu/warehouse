package com.yeqifu.bus.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yeqifu.bus.entity.Goods;
import com.yeqifu.bus.entity.Inport;
import com.yeqifu.bus.mapper.GoodsMapper;
import com.yeqifu.bus.mapper.InportMapper;
import com.yeqifu.bus.service.impl.InportServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * InportServiceImpl 单元测试
 * 测试核心业务：商品进货功能
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("进货服务单元测试")
class InportServiceImplTest {

    @Mock
    private GoodsMapper goodsMapper;

    @Mock
    private InportMapper inportMapper;

    @InjectMocks
    private InportServiceImpl inportService;

    /**
     * 初始化baseMapper（MyBatis-Plus父类需要）
     */
    private void initBaseMapper() {
        ReflectionTestUtils.setField(inportService, "baseMapper", inportMapper);
    }

    @Test
    @DisplayName("用例1：正常成功场景 - 商品进货成功，库存正确累加")
    void save_ShouldUpdateGoodsInventoryAndSaveInport_WhenValidInput() {
        // 初始化baseMapper
        initBaseMapper();
        
        // 前置条件：构建测试数据
        Integer goodsId = 1;
        Integer currentStock = 100;
        Integer inportNumber = 50;
        
        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setGoodsname("测试商品");
        goods.setNumber(currentStock);
        
        Inport inport = new Inport();
        inport.setGoodsid(goodsId);
        inport.setNumber(inportNumber);

        // Mock策略
        given(goodsMapper.selectById(goodsId)).willReturn(goods);
        given(goodsMapper.updateById(any(Goods.class))).willReturn(1);
        given(inportMapper.insert(any(Inport.class))).willReturn(1);

        // 执行测试
        boolean result = inportService.save(inport);

        // 断言点
        assertTrue(result, "进货操作应返回true");
        assertEquals(currentStock + inportNumber, goods.getNumber(), 
                "商品库存应更新为原库存 + 进货数量");
        verify(goodsMapper, times(1)).selectById(goodsId);
        verify(goodsMapper, times(1)).updateById(any(Goods.class));
        verify(inportMapper, times(1)).insert(any(Inport.class));
    }

    @Test
    @DisplayName("用例2：参数异常场景 - 进货数量为负数，库存被错误扣减")
    void save_ShouldHandleNegativeNumber_WhenInportNumberIsNegative() {
        // 初始化baseMapper
        initBaseMapper();
        
        // 前置条件：构建测试数据，进货数量为负数
        Integer goodsId = 1;
        Integer currentStock = 100;
        Integer negativeNumber = -50;  // 异常参数：负数
        
        Goods goods = new Goods();
        goods.setId(goodsId);
        goods.setGoodsname("测试商品");
        goods.setNumber(currentStock);
        
        Inport inport = new Inport();
        inport.setGoodsid(goodsId);
        inport.setNumber(negativeNumber);

        // Mock策略
        given(goodsMapper.selectById(goodsId)).willReturn(goods);
        given(goodsMapper.updateById(any(Goods.class))).willReturn(1);
        given(inportMapper.insert(any(Inport.class))).willReturn(1);

        // 执行测试
        boolean result = inportService.save(inport);

        // 断言点：验证当前实现的"问题"（无参数校验）
        assertTrue(result, "当前实现无参数校验，应返回true");
        assertEquals(currentStock + negativeNumber, goods.getNumber(), 
                "当前实现无校验，库存被错误更新为负数");
    }

    @Test
    @DisplayName("用例3：业务失败分支 - 商品不存在，抛出空指针异常")
    void save_ShouldThrowNullPointerException_WhenGoodsNotExists() {
        // 初始化baseMapper
        initBaseMapper();
        
        // 前置条件：商品不存在
        Integer nonExistentGoodsId = 999;
        
        Inport inport = new Inport();
        inport.setGoodsid(nonExistentGoodsId);
        inport.setNumber(50);

        // Mock策略：商品查询返回null
        given(goodsMapper.selectById(nonExistentGoodsId)).willReturn(null);

        // 执行测试 & 断言：应抛出空指针异常
        NullPointerException exception = assertThrows(NullPointerException.class, 
                () -> inportService.save(inport),
                "商品不存在时应抛出NullPointerException");

        // 断言：后续操作未执行
        verify(goodsMapper, never()).updateById(any(Goods.class));
        verify(inportMapper, never()).insert(any(Inport.class));
    }
}
