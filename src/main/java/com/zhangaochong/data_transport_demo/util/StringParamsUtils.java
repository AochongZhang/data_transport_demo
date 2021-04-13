package com.zhangaochong.data_transport_demo.util;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 10:00
 */
public abstract class StringParamsUtils {
    /**
     * 解析字符串参数
     *
     * @param params 字符串参数 格式 key1=value1,key2=value2...
     * @return 参数集合
     */
    public static Map<String, String> parseParams(String params) {
        if (StringUtils.isEmpty(params)) {
            return Collections.emptyMap();
        }
        params = params.replace("\n", "");
        String[] paramArray = params.split(",");
        if (paramArray.length == 0) {
            throw new IllegalArgumentException("参数" + params + "格式不正确");
        }
        Map<String, String> result = new HashMap<>(paramArray.length);
        for (String paramString : paramArray) {
            String[] kvArray = paramString.split("=");
            if (kvArray.length != 2) {
                throw new IllegalArgumentException("参数" + paramString + "格式不正确");
            }
            String key = kvArray[0];
            String value = kvArray[1];
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                throw new IllegalArgumentException("参数" + paramString + "格式不正确");
            }
            result.put(key, value);
        }
        return result;
    }
}
