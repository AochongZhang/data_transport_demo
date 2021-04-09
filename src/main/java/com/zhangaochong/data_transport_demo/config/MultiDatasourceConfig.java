package com.zhangaochong.data_transport_demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 13:25
 */
@Configuration
@EnableConfigurationProperties(DataTransportProperties.class)
public class MultiDatasourceConfig {

    /** 多数据源配置 */
    @Autowired
    private DataTransportProperties dataTransportProperties;

    /** 默认数据源配置 */
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Bean
    public MultiDatasource multiDatasource() {
        Map<Object, Object> datasourceMap = new HashMap<>(dataTransportProperties.getMultiDatasource().size() + 1);

        // 默认数据源 spring.datasource
        DruidDataSource defaultDataSource = dataSourceProperties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
        datasourceMap.put(MultiDatasourceThreadLocal.DEFAULT_DATASOURCE, defaultDataSource);

        // 多数据源 data-transport.multi-datasource
        Map<String, DataSourceProperties> multiDatasourceProperties = dataTransportProperties.getMultiDatasource();
        for (String datasourceName : multiDatasourceProperties.keySet()) {
            DataSourceProperties properties = multiDatasourceProperties.get(datasourceName);
            DruidDataSource dataSource = properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
            datasourceMap.put(datasourceName, dataSource);
        }

        MultiDatasource multiDatasource = new MultiDatasource();
        multiDatasource.setTargetDataSources(datasourceMap);
        return multiDatasource;
    }
}
