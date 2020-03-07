package com.yeqifu.bus.mapper;

import com.yeqifu.bus.entity.Provider;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * InnoDB free: 9216 kB Mapper 接口
 * </p>
 *
 * @author luoyi-
 * @since 2019-12-05
 */
public interface ProviderMapper extends BaseMapper<Provider> {

    /**
     * 根据供应商id删除商品信息
     * @param id
     */
    void deleteGoodsByProviderId(@Param("pid") Integer id);

    /**
     * 根据供应商id删除商品进货信息
     * @param id
     */
    void deleteInportByProviderId(@Param("pid") Integer id);

    /**
     * 根据供应商id删除商品退货信息
     * @param id
     */
    void deleteOutPortByProviderId(@Param("pid") Integer id);


}
