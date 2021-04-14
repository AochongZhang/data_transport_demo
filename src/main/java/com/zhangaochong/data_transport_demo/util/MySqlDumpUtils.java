package com.zhangaochong.data_transport_demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.net.URI;

/**
 * @author Aochong Zhang
 * @date 2021-04-14 09:08
 */
@Slf4j
public abstract class MySqlDumpUtils {
    /**
     * 构建mysqldump命令
     *
     * @param host 主机地址
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @param database 数据库
     * @param table 表
     * @param file 导出文件全路径
     * @return mysqldump命令
     */
    public static String buildCommand(String host, String port, String username, String password,
                                      String database, String table, String file) {
        StringBuilder command = new StringBuilder();
        command.append("mysqldump")
                .append(" -h ").append(host)
                .append(" -P ").append(port)
                .append(" -u").append(username)
                .append(" -p").append(password)
                .append(" --default-character-set=utf8mb4 ")
                .append(" --skip-tz-utc ")
                .append(" -r ").append(file)
                .append(" -t ").append(database).append(" ").append(table);
        return command.toString();
    }

    /**
     * 构建mysqldump命令
     *
     * @param dataSourceProperties 数据源配置
     * @param table 表
     * @param file 导出文件全路径
     * @return mysqldump命令
     */
    public static String buildCommand(DataSourceProperties dataSourceProperties, String table, String file) {
        URI uri = URI.create(dataSourceProperties.getUrl().substring(5));
        String host = uri.getHost();
        int port = uri.getPort();
        String database = uri.getPath().substring(1);
        return buildCommand(host, String.valueOf(port), dataSourceProperties.getUsername(), dataSourceProperties.getPassword(),
                database, table, file);
    }
}
