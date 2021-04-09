package com.zhangaochong.data_transport_demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 13:16
 */
@Slf4j
public class MultiDatasource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        String datasourceName = MultiDatasourceThreadLocal.getDatasourceName();
        log.info("[切换到数据源] {}", datasourceName);
        return datasourceName;
    }
}
