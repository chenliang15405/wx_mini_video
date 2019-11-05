package com.wx.mini.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author alan.chen
 * @date 2019/11/4 4:57 PM
 */
@Component
public class ApiCollDataUtil {

    @Autowired
    private RedisOperator redisOperator;


    /**
     * 使用redis去重
     *
     * @param str 每句内容
     */
    public boolean duplicateRemove(String keyPrefix, String str) {
        // 查询是否在redis中存在
        String val = redisOperator.get(keyPrefix + str);
        if(StringUtils.isBlank(val)) {
            redisOperator.set(keyPrefix + str, str);
            return true;
        }
        return false;
    }

}
