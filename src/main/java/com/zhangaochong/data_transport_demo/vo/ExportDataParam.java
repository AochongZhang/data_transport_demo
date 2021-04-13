package com.zhangaochong.data_transport_demo.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 18:03
 */
@Data
public class ExportDataParam {
    /** 数据源名 */
    private String datasourceName;

    /** 表名 */
    private String tableName;

    public static ExportDataParam formMap(Map<String, String> map) {
        String datasourceName = map.get("datasourceName");
        String tableName = map.get("tableName");

        ExportDataParam param = new ExportDataParam();
        param.setDatasourceName(datasourceName);
        param.setTableName(tableName);
        return param;
    }
}
