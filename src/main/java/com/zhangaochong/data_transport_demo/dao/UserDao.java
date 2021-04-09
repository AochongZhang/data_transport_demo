package com.zhangaochong.data_transport_demo.dao;

import com.zhangaochong.data_transport_demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Aochong Zhang
 * @date 2021-04-07 16:54
 */
@Mapper
public interface UserDao {
    List<User> selectTest(@Param("tableName") String tableName);

    void update(@Param("tableName") String tableName);
}
