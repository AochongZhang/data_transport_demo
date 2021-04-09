package com.zhangaochong.data_transport_demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Aochong Zhang
 * @date 2021-04-07 16:54
 */
@Mapper
public interface DataTransportDao {
    String isTableExist(@Param("tableName") String tableName);
}
