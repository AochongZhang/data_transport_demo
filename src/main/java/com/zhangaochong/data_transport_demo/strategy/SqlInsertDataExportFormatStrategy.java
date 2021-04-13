package com.zhangaochong.data_transport_demo.strategy;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 导出数据格式化为insert sql
 * <code>insert into 表 (列1, 列2, 列3...) values (值1, 值2, 值3...);</code>
 *
 * @author Aochong Zhang
 * @date 2021-04-13 10:47
 */
public class SqlInsertDataExportFormatStrategy implements DataExportFormatStrategy {

    @Override
    public String format(String datasourceName, String tableName, List<String> columnNameList, List<Map<String, Object>> dataList) {
        StringBuilder baseSql = new StringBuilder();
        baseSql.append("insert into ").append(datasourceName).append(".").append(tableName).append(" (");
        for (String columnName : columnNameList) {
            baseSql.append(columnName).append(", ");
        }
        baseSql.deleteCharAt(baseSql.length() - 1).deleteCharAt(baseSql.length() - 1).append(") values (");

        StringBuilder result = new StringBuilder();
        for (Map<String, Object> data : dataList) {
            result.append(baseSql);
            for (String columnName : columnNameList) {
                appendData(result, columnName, data);
            }
            result.deleteCharAt(result.length() - 1).deleteCharAt(result.length() - 1).append(");\n");
        }
        return result.toString();
    }

    private void appendData(StringBuilder result, String columnName, Map<String, Object> data) {
        Object obj = data.get(columnName);
        if (obj instanceof Integer || obj instanceof Long || obj instanceof Double) {
            result.append(obj).append(", ");
        } else if (obj instanceof Timestamp) {
            result.append("'").append(DATEFORMAT.get().format(((Timestamp) obj))).append("', ");
        } else {
            result.append("'").append(obj).append("', ");
        }
    }
}
