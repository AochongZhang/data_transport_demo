package com.zhangaochong.data_transport_demo.service;

import com.zhangaochong.data_transport_demo.config.MultiDatasourceThreadLocal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class DataTransportServiceTest {
    @Autowired
    private DataTransportService dataTransportService;

    @Test
    void createBackupTable() {
        MultiDatasourceThreadLocal.setDatasourceName("datasource1");
        String test1 = dataTransportService.createBackupTable("test1");
        System.out.println(test1);
    }

    @Test
    void transportData() {
        MultiDatasourceThreadLocal.setDatasourceName("datasource1");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime endTime = LocalDateTime.parse("2021-03-06 00:00:00", dateTimeFormatter);
        dataTransportService.transportData("test1", "test1_bak", 2, endTime, "update_date");
    }

    @Test
    void recoverData() {
        MultiDatasourceThreadLocal.setDatasourceName("datasource1");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse("2021-03-01 00:00:00", dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse("2021-03-26 00:00:00", dateTimeFormatter);
        dataTransportService.doRecoverData("test1_bak", "test1", startTime, endTime, "update_date", true);
    }

    @Test
    void isColumnExist() {
        MultiDatasourceThreadLocal.setDatasourceName("datasource1");
        boolean columnExist = dataTransportService.isColumnExist("test1", "name");
        System.out.println(columnExist);
    }

    @Test
    void getCreateTableSql() {
        MultiDatasourceThreadLocal.setDatasourceName("datasource1");
        String sql = dataTransportService.getCreateTableSql("test1");
        System.out.println(sql);
    }
}