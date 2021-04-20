package com.zhangaochong.data_transport_demo.vo;

import com.zhangaochong.data_transport_demo.enums.DataTransportTimeUnit;
import lombok.Data;
import org.springframework.util.StringUtils;

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
        if (!StringUtils.hasText(datasourceName)) {
            throw new IllegalArgumentException("datasourceName参数不正确");
        }
        String tableName = map.get("tableName");
        if (!StringUtils.hasText(tableName)) {
            throw new IllegalArgumentException("tableName参数不正确");
        }
        if (map.get("stepLength") == null && Integer.parseInt(map.get("stepLength")) <= 0) {
            throw new IllegalArgumentException("time参数不正确");
        }
        Integer stepLength = Integer.valueOf(map.get("stepLength"));
        if (map.get("time") == null && Integer.parseInt(map.get("time")) <= 0) {
            throw new IllegalArgumentException("time参数不正确");
        }
        Integer time = Integer.parseInt(map.get("time"));
        if (map.get("timeUnit") == null) {
            throw new IllegalArgumentException("timeUnit参数不正确");
        }
        DataTransportTimeUnit timeUnit = DataTransportTimeUnit.valueOf(map.get("timeUnit"));
        String dateColumn = map.get("dateColumn");
        if (!StringUtils.hasText(dateColumn)) {
            throw new IllegalArgumentException("dateColumn参数不正确");
        }

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
