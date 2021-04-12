package com.zhangaochong.data_transport_demo.vo;

import com.zhangaochong.data_transport_demo.jobhandler.AochongJobHandler;
import lombok.Data;

import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-12 09:59
 */
@Data
public class DataTransportJobParam {
    private String datasourceName;
    private String tableName;
    private Integer stepLength;

    public static DataTransportJobParam formMap(Map<String, String> map) {
        String datasourceName = map.get("datasourceName");
        String tableName = map.get("tableName");
        Integer stepLength = map.get("stepLength") == null ? null : Integer.valueOf(map.get("stepLength"));
        DataTransportJobParam jobParam = new DataTransportJobParam();
        jobParam.setDatasourceName(datasourceName);
        jobParam.setTableName(tableName);
        jobParam.setStepLength(stepLength);
        return jobParam;
    }
}
