package com.zhangaochong.data_transport_demo.util;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 导出数据格式化为csv
 *
 * @author Aochong Zhang
 * @date 2021-04-13 15:15
 */
public class CsvDataExportFormatStrategyImpl implements DataExportFormatStrategy {

    @Override
    public String format(String datasourceName, String tableName, List<String> columnNameList, List<Map<String, Object>> dataList) {
        StringBuilder result = new StringBuilder();
        for (String columnName : columnNameList) {
            result.append(columnName).append(",");
        }
        result.deleteCharAt(result.length() - 1).append("\n");

        for (Map<String, Object> data : dataList) {
            for (String columnName : columnNameList) {
                appendData(result, columnName, data);
            }
            result.deleteCharAt(result.length() - 1).append("\n");
        }
        return result.toString();
    }

    private void appendData(StringBuilder result, String columnName, Map<String, Object> data) {
        Object obj = data.get(columnName);
        if (obj instanceof Timestamp) {
            result.append(DATEFORMAT.get().format(((Timestamp) obj))).append(",");
        } else {
            result.append(obj).append(",");
        }
    }
}
