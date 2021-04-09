package com.zhangaochong.data_transport_demo.service;

import com.zhangaochong.data_transport_demo.dao.DataTransportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 16:16
 */
@Service
public class DataTransportService {
    @Autowired
    private DataTransportDao dataTransportDao;

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
}
