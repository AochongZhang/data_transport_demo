package com.zhangaochong.data_transport_demo.config;

import org.springframework.util.StringUtils;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 13:38
 */
public class MultiDatasourceThreadLocal {
    public static final String DEFAULT_DATASOURCE = "defaultDatasource";

    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> MultiDatasourceThreadLocal.DEFAULT_DATASOURCE);

    public static void setDatasourceName(String datasourceName) {
        if (StringUtils.isEmpty(datasourceName)) {
            THREAD_LOCAL.set(DEFAULT_DATASOURCE);
        } else {
            THREAD_LOCAL.set(datasourceName);
        }
    }

    public static String getDatasourceName() {
        return THREAD_LOCAL.get();
    }

    public static void removeDatasourceName() {
        THREAD_LOCAL.remove();
    }
}
