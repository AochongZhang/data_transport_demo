package com.zhangaochong.data_transport_demo.vo;

import lombok.Data;

/**
 * @author Aochong Zhang
 * @date 2021-04-08 17:18
 */
@Data
public class DataTransportVO {
    private String sourceDatasource;
    private String sourceTable;
    private String targetDatasource;
    private String targetTable;
}
