package com.yeqifu.sys.controller;

import com.yeqifu.sys.cache.CachePool;
import com.yeqifu.sys.common.CacheBean;
import com.yeqifu.sys.common.DataGridView;
import com.yeqifu.sys.common.ResultObj;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存管理控制器
 * @Author: 落亦-
 * @Date: 2019/12/20 18:36
 */
@RestController
@RequestMapping("cache")
public class CacheController {

    private static volatile Map<String,Object> CACHE_CONTAINER= CachePool.CACHE_CONTAINER;

    /**
     * 查询所有缓存
     * @return
     */
    @RequestMapping("loadAllCache")
    public DataGridView loadAllCache(){
        List<CacheBean> list = new ArrayList<>();

        Set<Map.Entry<String, Object>> entrySet = CACHE_CONTAINER.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            list.add(new CacheBean(entry.getKey(),entry.getValue()));
        }
        return new DataGridView(list);
    }

    /**
     * 删除缓存
     * @param key
     * @return
     */
    @RequestMapping("deleteCache")
    public ResultObj deleteCache(String key){
        CachePool.removeCacheByKey(key);
        return ResultObj.DELETE_SUCCESS;
    }

    /**
     * 清空所有缓存
     * @return
     */
    @RequestMapping("removeAllCache")
    public ResultObj removeAllCache(){
        CachePool.removeAll();
        return ResultObj.DELETE_SUCCESS;
    }

    /**
     * 同步缓存
     * @return
     */
    @RequestMapping("syncCache")
    public ResultObj syncCache(){
        CachePool.syncData();
        return ResultObj.SYNCCACHE_SUCCESS;
    }

}
