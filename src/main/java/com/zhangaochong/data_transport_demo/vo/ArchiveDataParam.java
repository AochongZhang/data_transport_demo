package com.zhangaochong.data_transport_demo.vo;

import com.zhangaochong.data_transport_demo.enums.DataTransportTimeUnit;
import lombok.Data;

import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 09:59
 */
@Data
public class ArchiveDataParam {
    /** 数据源名 */
    private String datasourceName;

    /** 表名 */
    private String tableName;

    /** 归档文件最大文件大小, 单位支持B、KB、MB、GB、TB、K、M、G、T */
    private String fileMaxSize;

    /** 迁移数据时间 */
    private Integer time;

    /** 时间单位 */
    private DataTransportTimeUnit timeUnit;

    /** 表中时间列 */
    private String dateColumn;

    public static ArchiveDataParam formMap(Map<String, String> map) {
        String datasourceName = map.get("datasourceName");
        String tableName = map.get("tableName");
        String fileMaxSize = map.get("fileMaxSize");
        Integer time = map.get("time") == null ? null : Integer.valueOf(map.get("time"));
        DataTransportTimeUnit timeUnit = map.get("timeUnit") == null ? null : DataTransportTimeUnit.valueOf(map.get("timeUnit"));
        String dateColumn = map.get("dateColumn");

        ArchiveDataParam param = new ArchiveDataParam();
        param.setDatasourceName(datasourceName);
        param.setTableName(tableName);
        param.setFileMaxSize(fileMaxSize);
        param.setTime(time);
        param.setTimeUnit(timeUnit);
        param.setDateColumn(dateColumn);
        return param;
    }
}
