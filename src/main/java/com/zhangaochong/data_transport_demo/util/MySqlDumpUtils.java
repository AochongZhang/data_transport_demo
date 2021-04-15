package com.zhangaochong.data_transport_demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aochong Zhang
 * @date 2021-04-14 09:08
 */
@Slf4j
public abstract class MySqlDumpUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

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

    /**
     * 构建文件名
     * 示例：
     *  参数 datasourceName=d1, tableName=t1, pattern={datasourceName}-{tableName}-{datetime}
     *  返回 datasource1-test1-20210414152347.sql
     *
     * @param datasourceName 数据库源
     * @param tableName 表名
     * @param pattern 文件名规则，支持{datasourceName} {tableName} {datetime}
     * @return
     */
    public static String buildFileName(String datasourceName, String tableName, String pattern) {
        String fileName = pattern;
        fileName = fileName.replace("{datasourceName}", datasourceName);
        fileName = fileName.replace("{tableName}", tableName);
        String datatime = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        fileName = fileName.replace("{datetime}", datatime);
        fileName += ".sql";
        return fileName;
    }

    /**
     * 构建替换文件中表名命令
     *
     * @param fromTableName 被替换的表名
     * @param toTableName 新表名
     * @param fileName 文件名
     * @return 执行命令
     */
    public static String[] buildReplaceTableNameCommand(String fromTableName, String toTableName, String fileName) {
        List<String> commandArray = new ArrayList<>();
        commandArray.add("sed");
        commandArray.add("-i");
        String osName = System.getProperties().getProperty("os.name");
        // mac下sed命令需增加参数
        if ("Mac OS X".equals(osName)) {
            commandArray.add("");
        }
        commandArray.add("s/`" + fromTableName + "`/`"+ toTableName + "`/g");
        commandArray.add(fileName);
        return commandArray.toArray(new String[]{});
    }
}
