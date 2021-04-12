package com.zhangaochong.data_transport_demo.vo;

import com.zhangaochong.data_transport_demo.enums.DataTransportTimeUnit;
import lombok.Data;

import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 09:59
 */
@Data
public class BackupDataParam {
    /** 数据源名 */
    private String datasourceName;

    /** 表名 */
    private String tableName;

    /** 迁移步长 */
    private Integer stepLength;

    /** 迁移数据时间 */
    private Integer time;

    /** 时间单位 */
    private DataTransportTimeUnit timeUnit;

    /** 表中时间列 */
    private String dateColumn;

    public static BackupDataParam formMap(Map<String, String> map) {
        String datasourceName = map.get("datasourceName");
        String tableName = map.get("tableName");
        Integer stepLength = map.get("stepLength") == null ? null : Integer.valueOf(map.get("stepLength"));
        Integer time = map.get("time") == null ? null : Integer.valueOf(map.get("time"));
        DataTransportTimeUnit timeUnit = map.get("timeUnit") == null ? null : DataTransportTimeUnit.valueOf(map.get("timeUnit"));
        String dateColumn = map.get("dateColumn");

        BackupDataParam param = new BackupDataParam();
        param.setDatasourceName(datasourceName);
        param.setTableName(tableName);
        param.setStepLength(stepLength);
        param.setTime(time);
        param.setTimeUnit(timeUnit);
        param.setDateColumn(dateColumn);
        return param;
    }
}
