package com.zhangaochong.data_transport_demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Aochong Zhang
 * @date 2021-04-07 16:54
 */
@Mapper
public interface DataTransportDao {
    String isTableExist(@Param("tableName") String tableName);

    void createTableLike(@Param("oldTableName") String oldTableName, @Param("newTableName") String newTableName);

    int copyData(@Param("fromTableName") String fromTableName, @Param("toTableName") String toTableName,
                 @Param("rowNum") Integer rowNum, @Param("endTime") LocalDateTime endTime, @Param("dateColumn") String dateColumn);

    int delete(@Param("tableName") String tableName, @Param("rowNum") Integer rowNum);

    int recoverData(@Param("fromTableName") String fromTableName, @Param("toTableName") String toTableName,
                     @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                     @Param("dateColumn") String dateColumn, @Param("isOverwrite") Boolean isOverwrite);

    String isColumnExist(@Param("tableName") String tableName, @Param("column") String column);
}
