package com.zhangaochong.data_transport_demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-07 16:54
 */
@Mapper
public interface DataTransportDao {
    /**
     * 判断表是否存在
     *
     * @param tableName 表名
     * @return 表名
     */
    String isTableExist(@Param("tableName") String tableName);

    /**
     * 复制表结构并创建新表
     *
     * @param oldTableName 原表
     * @param newTableName 新表
     */
    void createTableLike(@Param("oldTableName") String oldTableName, @Param("newTableName") String newTableName);

    /**
     * 从一张表复制数据到另一张表
     *
     * @param fromTableName 原表名
     * @param toTableName 目标表名
     * @param rowNum 行数
     * @param endTime 截止时间
     * @param dateColumn 时间列
     * @return 实际复制行数
     */
    int copyData(@Param("fromTableName") String fromTableName, @Param("toTableName") String toTableName,
                 @Param("rowNum") Integer rowNum, @Param("endTime") LocalDateTime endTime, @Param("dateColumn") String dateColumn);

    /**
     * 删除表数据
     *
     * @param tableName 表名
     * @param rowNum 行数
     * @return
     */
    int delete(@Param("tableName") String tableName, @Param("rowNum") Integer rowNum);

    /**
     * 恢复一张表数据到另一张表
     *
     * @param fromTableName 原表名
     * @param toTableName 目标表名
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param dateColumn 时间列
     * @param isOverwrite 是否覆盖原数据
     * @return 实际恢复行数
     */
    int recoverData(@Param("fromTableName") String fromTableName, @Param("toTableName") String toTableName,
                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                     @Param("dateColumn") String dateColumn, @Param("isOverwrite") Boolean isOverwrite);

    /**
     * 判断表中是否有指定列
     *
     * @param tableName 表名
     * @param column 列名
     * @return 列名
     */
    String isColumnExist(@Param("tableName") String tableName, @Param("column") String column);

    /**
     * 获取建表语句
     *
     * @param tableName 表名
     * @return 建表语句
     */
    Map<String, String> showCreateTable(@Param("tableName") String tableName);

    List<Map<String, Object>> selectData(@Param("tableName") String tableName);

    /**
     * 获取表所有字段名
     *
     * @param tableName 表名
     * @return 表中所有字段名
     */
    List<String> getColumnName(@Param("tableName") String tableName);

    /**
     * 查询表数据占用空间大小
     *
     * @param tableName 表名
     * @return 占用空间大小，单位Byte
     */
    Long getDataLength(@Param("tableName") String tableName);

    void dropTable(@Param("tableName") String tableName);

    void truncate(@Param("tableName") String tableName);

    Integer getTableRow(@Param("tableName") String tableName);
}
