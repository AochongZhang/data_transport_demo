package com.zhangaochong.data_transport_demo.config;

import com.zhangaochong.data_transport_demo.util.SqlInsertDataExportFormatStrategy;
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
    /** 备份表后缀 */
    private String backupTablePostfix = "_bak";
    /** 导出配置 */
    private DataExport dataExport;

    @Data
    public static class DataExport {
        /** 导出文件路径 */
        private String filePath;
        /** 导出文件格式化策略全类名 */
        private String formatStrategy = SqlInsertDataExportFormatStrategy.class.getName();
        /** 导出文件后缀名 */
        private String filePostfix = ".sql";
    }
}
