package com.zhangaochong.data_transport_demo.vo;

import com.zhangaochong.data_transport_demo.util.DataTransportTimeUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 18:03
 */
@Data
public class RecoverDataParam {
    /** 数据源名 */
    private String datasourceName;

    /** 表名 */
    private String tableName;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 表中时间列 */
    private String dateColumn;

    /** 是否覆盖原数据 */
    private Boolean isOverwrite;

    public static RecoverDataParam formMap(Map<String, String> map) {
        String datasourceName = map.get("datasourceName");
        if (!StringUtils.hasText(datasourceName)) {
            throw new IllegalArgumentException("datasourceName参数不正确");
        }
        String tableName = map.get("tableName");
        if (!StringUtils.hasText(tableName)) {
            throw new IllegalArgumentException("tableName参数不正确");
        }
        LocalDateTime startTime = DataTransportTimeUtils.parseFromString(map.get("startTime"));
        LocalDateTime endTime = DataTransportTimeUtils.parseFromString(map.get("endTime"));
        String dateColumn = map.get("dateColumn");
        if (!StringUtils.hasText(dateColumn)) {
            throw new IllegalArgumentException("dateColumn参数不正确");
        }
        Boolean isOverwrite = Boolean.valueOf(map.get("isOverwrite"));

        RecoverDataParam param = new RecoverDataParam();
        param.setDatasourceName(datasourceName);
        param.setTableName(tableName);
        param.setStartTime(startTime);
        param.setEndTime(endTime);
        param.setDateColumn(dateColumn);
        param.setIsOverwrite(isOverwrite);

        param.setDateColumn(dateColumn);
        return param;
    }
}
