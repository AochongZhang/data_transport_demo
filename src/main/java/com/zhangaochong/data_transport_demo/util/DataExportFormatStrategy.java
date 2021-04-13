package com.zhangaochong.data_transport_demo.util;

import java.util.List;
import java.util.Map;

/**
 * 数据导出格式化策略
 *
 * @author Aochong Zhang
 * @date 2021-04-13 10:43
 */
@FunctionalInterface
public interface DataExportFormatStrategy {
    /**
     * 格式化数据为字符串
     *
     * @param tableName 表名
     * @param columnNameList 表中所有列名
     * @param dataList 数据
     * @return 格式化后字符串
     */
    String format(String datasourceName, String tableName, List<String> columnNameList, List<Map<String, Object>> dataList);
}