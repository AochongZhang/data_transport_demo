package com.zhangaochong.data_transport_demo.service;

import com.xxl.job.core.log.XxlJobLogger;
import com.zhangaochong.data_transport_demo.config.DataTransportProperties;
import com.zhangaochong.data_transport_demo.config.MultiDatasourceThreadLocal;
import com.zhangaochong.data_transport_demo.config.OssProperties;
import com.zhangaochong.data_transport_demo.dao.DataTransportDao;
import com.zhangaochong.data_transport_demo.strategy.DataExportFormatStrategy;
import com.zhangaochong.data_transport_demo.util.*;
import com.zhangaochong.data_transport_demo.vo.ArchiveDataParam;
import com.zhangaochong.data_transport_demo.vo.BackupDataParam;
import com.zhangaochong.data_transport_demo.vo.RecoverDataParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 16:16
 */
@Slf4j
@Service
public class DataTransportService {
    @Autowired
    private DataTransportDao dataTransportDao;

    @Autowired
    private DataTransportProperties dataTransportProperties;

    @Autowired
    private OssProperties ossProperties;

    /**
     * 备份数据
     *
     * @param params 备份参数
     * @return 实际备份条数
     */
    public int backupData(BackupDataParam params) {
        log.info("[数据备份] 开始============================================");
        XxlJobLogger.log("[数据备份] 开始============================================");
        log.info("[数据备份] 参数={}", params);
        XxlJobLogger.log("[数据备份] 参数={}", params);
        //  切换数据源
        MultiDatasourceThreadLocal.setDatasourceName(params.getDatasourceName());
        // 表名校验
        if (!isTableExist(params.getTableName())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "不存在");
        }
        // 时间列校验
        if (!isColumnExist(params.getTableName(), params.getDateColumn())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "时间列" + params.getDateColumn() + "不存在");
        }
        // 如果备份表不存在创建备份表
        String backupTable = createBackupTable(params.getTableName());
        // 迁移数据
        int row = transportData(params.getTableName(), backupTable, params.getStepLength(),
                DataTransportTimeUtils.minusTime(LocalDateTime.now(), params.getTime(), params.getTimeUnit()),
                params.getDateColumn());
        MultiDatasourceThreadLocal.removeDatasourceName();
        log.info("[数据备份] 备份条数={}", row);
        XxlJobLogger.log("[数据备份] 备份条数={}", row);
        log.info("[数据备份] 结束============================================");
        XxlJobLogger.log("[数据备份] 结束============================================");
        return row;
    }

    /**
     * 恢复数据
     *
     * @param params 恢复参数
     * @return 实际恢复条数
     */
    public int recoverData(RecoverDataParam params) {
        log.info("[数据恢复] 开始============================================");
        XxlJobLogger.log("[数据恢复] 开始============================================");
        log.info("[数据恢复] 参数={}", params);
        XxlJobLogger.log("[数据恢复] 参数={}", params);
        //  切换数据源
        MultiDatasourceThreadLocal.setDatasourceName(params.getDatasourceName());
        // 表名校验
        if (!isTableExist(params.getTableName())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "不存在");
        }
        String backupTableName = getBackupTableName(params.getTableName());
        if (!isTableExist(backupTableName)) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "的备份表" + backupTableName + "不存在");
        }
        // 时间列校验
        if (!isColumnExist(params.getTableName(), params.getDateColumn())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "时间列" + params.getDateColumn() + "不存在");
        }
        int row = doRecoverData(backupTableName, params.getTableName(), params.getStartTime(), params.getEndTime(),
                params.getDateColumn(), params.getIsOverwrite());
        MultiDatasourceThreadLocal.removeDatasourceName();
        log.info("[数据备份] 恢复条数={}", row);
        XxlJobLogger.log("[数据备份] 恢复条数={}", row);
        log.info("[数据恢复] 结束============================================");
        XxlJobLogger.log("[数据恢复] 结束============================================");
        return row;
    }

    /**
     * 迁移数据
     *
     * @param fromTableName 源表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @param toTableName 目标表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @param stepLength 迁移步长
     * @param endTime 结束时间
     * @return 实际迁移条数
     */
    @Transactional(rollbackFor = Exception.class)
    public int transportData(String fromTableName, String toTableName, Integer stepLength,
                              LocalDateTime endTime, String dateColumn) {
        int copyRow = dataTransportDao.copyData(fromTableName, toTableName, stepLength, endTime, dateColumn);
        log.info("[数据迁移] 拷贝数据 from={}, to={}, limit={}, resultRowNum={}", fromTableName, toTableName, stepLength, copyRow);
        XxlJobLogger.log("[数据迁移] 拷贝数据 from={}, to={}, limit={}, resultRowNum={}", fromTableName, toTableName, stepLength, copyRow);
        int deleteRow = dataTransportDao.delete(fromTableName, copyRow);
        log.info("[数据迁移] 删除数据 table={}, limit={}, resultRowNum={}", fromTableName, copyRow, deleteRow);
        XxlJobLogger.log("[数据迁移] 删除数据 table={}, limit={}, resultRowNum={}", fromTableName, copyRow, deleteRow);
        return copyRow;
    }

    /**
     * 恢复数据
     *
     * @param fromTableName 源表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @param toTableName 目标表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param dateColumn 时间列名 有SQL注入风险，列名需经过校验，{@link #isColumnExist}
     * @param isOverwrite 是否覆盖原数据
     * @return 实际恢复条数
     */
    @Transactional(rollbackFor = Exception.class)
    public int doRecoverData(String fromTableName, String toTableName,
                              LocalDateTime startTime, LocalDateTime endTime,
                              String dateColumn, Boolean isOverwrite) {
        int recoverDataRow = dataTransportDao.recoverData(fromTableName, toTableName, startTime, endTime, dateColumn, isOverwrite);
        log.info("[数据迁移] 恢复数据 from={}, to={}, startTime={}, endTime={}, dateColumn={}, isOverwrite={}, recoverDataRow={}",
                fromTableName, toTableName, startTime, endTime, dateColumn, isOverwrite, recoverDataRow);
        XxlJobLogger.log("[数据迁移] 恢复数据 from={}, to={}, startTime={}, endTime={}, dateColumn={}, isOverwrite={}, recoverDataRow={}",
                fromTableName, toTableName, startTime, endTime, dateColumn, isOverwrite, recoverDataRow);
        return recoverDataRow;
    }

    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return 表是否存在
     */
    public boolean isTableExist(String tableName) {
        String tableExist = dataTransportDao.isTableExist(tableName);
        return !StringUtils.isEmpty(tableExist);
    }

    /**
     * 判断表中列是否存在
     *
     * @param tableName 表名
     * @param column 列名
     * @return 表中列是否存在
     */
    public boolean isColumnExist(String tableName, String column) {
        String columnExist = dataTransportDao.isColumnExist(tableName, column);
        return !StringUtils.isEmpty(columnExist);
    }

    /**
     * 创建备份表
     *
     * @param tableName 表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @return 备份表名
     */
    public String createBackupTable(String tableName) {
        String backupTableName = getBackupTableName(tableName);
        dataTransportDao.createTableLike(tableName, backupTableName);
        return backupTableName;
    }

    /**
     * 获取备份表名
     *
     * @param tableName 原表名
     * @return 备份表名
     */
    public String getBackupTableName(String tableName) {
        String backupTablePostfix = StringUtils.isEmpty(dataTransportProperties.getBackupTablePostfix()) ? "_bak"
                : dataTransportProperties.getBackupTablePostfix();
        return tableName + backupTablePostfix;
    }

    /**
     * 获取建表语句
     *
     * @param tableName 表名 有SQL注入风险，表名需经过校验，{@link #isTableExist}
     * @return 建表语句
     */
    public String getCreateTableSql(String tableName) {
        Map<String, String> createTable = dataTransportDao.showCreateTable(tableName);
        return createTable.get("Create Table");
    }

    /**
     * 获取表所有字段名
     *
     * @param tableName 表名
     * @return 表中所有字段名
     */
    public List<String> getColumnName(String tableName) {
        return dataTransportDao.getColumnName(tableName);
    }

    public List<Map<String, Object>> selectData(String tableName) {
        // TODO 导出数据参数
        return dataTransportDao.selectData(tableName);
    }

    /**
     * 导出数据到文件
     *
     * @param datasourceName 数据源名
     * @param tableName 表名
     * @param columnNameList 表中所有列名
     * @param dataList 数据
     * @return 导出文件的全路径
     */
    public String exportData(String datasourceName, String tableName, List<String> columnNameList, List<Map<String, Object>> dataList) {
        DataTransportProperties.DataExport dataExport = dataTransportProperties.getDataExport();

        // 创建格式化策略
        DataExportFormatStrategy strategy = null;
        try {
            strategy = (DataExportFormatStrategy) Class.forName(dataExport.getFormatStrategy()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (strategy == null) {
            throw new RuntimeException("格式化策略创建失败");
        }
        String format = strategy.format(datasourceName, tableName, columnNameList, dataList);
        // TODO 导出文件名
        return DataTransportFileUtils.exportFile(dataExport.getFilePath(), "test1" + dataExport.getFilePostfix(), format);
    }

    /**
     * 数据归档
     *
     * @param params 归档参数
     */
    public int archiveData(ArchiveDataParam params) {
        log.info("[数据归档] 开始============================================");
        XxlJobLogger.log("[数据归档] 开始============================================");
        log.info("[数据归档] 参数={}", params);
        XxlJobLogger.log("[数据归档] 参数={}", params);
        // 迁移数据到临时表
        //  切换数据源
        MultiDatasourceThreadLocal.setDatasourceName(params.getDatasourceName());
        // 表名校验
        if (!isTableExist(params.getTableName())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "不存在");
        }
        // 时间列校验
        if (!isColumnExist(params.getTableName(), params.getDateColumn())) {
            throw new IllegalArgumentException("数据源" + params.getDatasourceName() + "中表" + params.getTableName()
                    + "时间列" + params.getDateColumn() + "不存在");
        }
        String bakTableName = params.getTableName() + dataTransportProperties.getBackupTablePostfix();
        String tempTableName = bakTableName + "_temp";
        log.info("[数据归档] 创建临时表{}", tempTableName);
        XxlJobLogger.log("[数据归档] 创建临时表{}", tempTableName);
        dataTransportDao.createTableLike(bakTableName, tempTableName);
        long fileMaxSize = FileSizeUtils.parseToByte(params.getFileMaxSize());
        // 分批循环迁移数据
        int row = 0;
        int countRow = 0;
        do {
            log.info("[数据归档] 分批迁移到临时表开始，步长={}", dataTransportProperties.getArchiveData().getStepLength());
            XxlJobLogger.log("[数据归档] 分批迁移到临时表开始，步长={}", dataTransportProperties.getArchiveData().getStepLength());
            row = transportData(bakTableName, tempTableName, dataTransportProperties.getArchiveData().getStepLength(),
                    DataTransportTimeUtils.minusTime(LocalDateTime.now(), params.getTime(), params.getTimeUnit()),
                    params.getDateColumn());
            countRow += row;
            Long dataLength = getDataLength(tempTableName);
            // 临时表数据量达到指定最大文件大小，执行dump临时表为文件
            if (row != 0 && dataLength >= fileMaxSize) {
                log.info("[数据归档] 分批迁移中, 临时表数据量达到最大文件大小, 临时表大小={}Byte, 最大文件大小={}Byte", dataLength, fileMaxSize);
                XxlJobLogger.log("[数据归档] 分批迁移中, 临时表数据量达到最大文件大小, 临时表大小={}Byte, 最大文件大小={}Byte", dataLength, fileMaxSize);
                dumpFile(params.getDatasourceName(), params.getTableName(), tempTableName);
                dataTransportDao.truncate(tempTableName);
            }
        } while (row != 0);
        Integer tableRow = dataTransportDao.getTableRow(tempTableName);
        if (tableRow != 0) {
            log.info("[数据归档] 分批迁移结束, 最后临时表数据量={}", tableRow);
            XxlJobLogger.log("[数据归档] 分批迁移结束, 最后临时表数据量={}", tableRow);
            dumpFile(params.getDatasourceName(), params.getTableName(), tempTableName);
        }
        dataTransportDao.dropTable(tempTableName);
        // 压缩上传文件
        MultiDatasourceThreadLocal.removeDatasourceName();
        log.info("[数据归档] 归档条数={}", countRow);
        XxlJobLogger.log("[数据归档] 归档条数={}", countRow);
        log.info("[数据归档] 结束============================================");
        XxlJobLogger.log("[数据归档] 结束============================================");
        return countRow;
    }

    /**
     * 使用mysqldump导出表数据
     *
     * @param datasourceName 数据源名
     * @param sourceTableName 原表名
     * @param tempTableName 临时表名
     */
    public void dumpFile(String datasourceName, String sourceTableName, String tempTableName) {
        Map<String, DataSourceProperties> multiDatasource = dataTransportProperties.getMultiDatasource();
        DataTransportProperties.ArchiveData archiveData = dataTransportProperties.getArchiveData();
        DataSourceProperties datasource1 = multiDatasource.get(datasourceName);
        String path = archiveData.getFilePath().endsWith("/") ? archiveData.getFilePath() : archiveData.getFilePath() + "/";
        File file = new File(path);
        if (file.mkdirs()) {
            log.info("创建目录 {}", path);
            XxlJobLogger.log("创建目录 {}", path);
        }
        String fileName = path + MySqlDumpUtils.buildFileName(datasourceName, tempTableName, archiveData.getFileNamePattern());
        log.info("mysqldump文件名={}", fileName);
        XxlJobLogger.log("mysqldump文件名={}", fileName);
        String build = MySqlDumpUtils.build(datasource1, sourceTableName, tempTableName, fileName);
        CommandUtils.execMultiCommand(build);
        uploadFile(new File(fileName + MySqlDumpUtils.COMPRESS_POSTFIX));
    }

    /**
     * 查询表数据占用空间大小
     *
     * @param tableName 表名
     * @return 占用空间大小，单位Byte
     */
    public Long getDataLength(String tableName) {
        return dataTransportDao.getDataLength(tableName);
    }

    public void uploadFile(File file) {
        String uploadPath = dataTransportProperties.getArchiveData().getUploadPath();
        OssUtils.upload(ossProperties, file, uploadPath);
    }
}
