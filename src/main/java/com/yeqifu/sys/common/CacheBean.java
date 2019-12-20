package com.yeqifu.sys.common;

import com.alibaba.fastjson.JSON;

/**
 * @Author: 落亦-
 * @Date: 2019/12/20 18:40
 */
public class CacheBean {

    private String key;

    private Object value;

    public CacheBean() {
    }

    public CacheBean(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return JSON.toJSON(value).toString();
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
