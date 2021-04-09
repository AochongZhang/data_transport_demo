package com.zhangaochong.data_transport_demo.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 数据迁移配置
 *
 * @author Aochong Zhang
 * @date 2021-04-08 14:29
 */
@Data
@ConfigurationProperties(prefix = "data-transport")
public class DataTransportProperties {
    /** 多数据源配置 */
    private Map<String, DataSourceProperties> multiDatasource;
}
