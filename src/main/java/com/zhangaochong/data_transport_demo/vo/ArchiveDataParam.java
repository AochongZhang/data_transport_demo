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
        if (!StringUtils.hasText(datasourceName)) {
            throw new IllegalArgumentException("datasourceName参数不正确");
        }
        String tableName = map.get("tableName");
        if (!StringUtils.hasText(tableName)) {
            throw new IllegalArgumentException("tableName参数不正确");
        }
        String fileMaxSize = map.get("fileMaxSize");
        if (!StringUtils.hasText(fileMaxSize)) {
            throw new IllegalArgumentException("fileMaxSize参数不正确");
        }
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
