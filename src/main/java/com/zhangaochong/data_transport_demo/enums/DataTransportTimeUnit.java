package com.zhangaochong.data_transport_demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 16:54
 * @see com.zhangaochong.data_transport_demo.util.DataTransportTimeUtils
 */
@AllArgsConstructor
@Getter
public enum DataTransportTimeUnit {
    HOUR("HOUR", "小时"),
    DAY("DAY", "天"),
    WEEK("WEEK", "周"),
    MONTH("MONTH", "月"),
    YEAR("YEAR", "年"),
    ;
    private String code;
    private String name;
}
