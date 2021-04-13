package com.zhangaochong.data_transport_demo.service;

import com.zhangaochong.data_transport_demo.config.DataTransportProperties;
import com.zhangaochong.data_transport_demo.config.MultiDatasourceThreadLocal;
import com.zhangaochong.data_transport_demo.dao.DataTransportDao;
import com.zhangaochong.data_transport_demo.util.DataTransportTimeUtils;
import com.zhangaochong.data_transport_demo.vo.BackupDataParam;
import com.zhangaochong.data_transport_demo.vo.RecoverDataParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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

    /**
     * 备份数据
     *
     * @param params 备份参数
     * @return 实际备份条数
     */
    public int backupData(BackupDataParam params) {
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
        return row;
    }

    /**
     * 恢复数据
     *
     * @param params 恢复参数
     * @return 实际恢复条数
     */
    public int recoverData(RecoverDataParam params) {
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
        int deleteRow = dataTransportDao.delete(fromTableName, copyRow);
        log.info("[数据迁移] 删除数据 table={}, limit={}, resultRowNum={}", fromTableName, copyRow, deleteRow);
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
}
