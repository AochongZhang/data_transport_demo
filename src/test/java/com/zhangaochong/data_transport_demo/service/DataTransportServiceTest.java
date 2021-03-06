package com.zhangaochong.data_transport_demo.service;

import com.zhangaochong.data_transport_demo.config.DataTransportProperties;
import com.zhangaochong.data_transport_demo.config.MultiDatasourceThreadLocal;
import com.zhangaochong.data_transport_demo.enums.DataTransportTimeUnit;
import com.zhangaochong.data_transport_demo.vo.ArchiveDataParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@SpringBootTest
class DataTransportServiceTest {
    @Autowired
    private DataTransportService dataTransportService;

    @Autowired
    private DataTransportProperties dataTransportProperties;

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

    @Test
    void getColumnName() {
        List<String> test1 = dataTransportService.getColumnName("test1");
        System.out.println(test1);
    }

    @Test
    void selectData() {
        MultiDatasourceThreadLocal.setDatasourceName("xxl_job");
        String tableName = "xxl_job_log";
        List<String> columnNameList = dataTransportService.getColumnName(tableName);
        List<Map<String, Object>> dataList = dataTransportService.selectData(tableName);
        System.out.println(columnNameList);
        System.out.println(dataList);
    }

    @Test
    void exportData() {
        String datasourceName = "datasource1";
        MultiDatasourceThreadLocal.setDatasourceName(datasourceName);
        String tableName = "test1";
        List<String> columnNameList = dataTransportService.getColumnName(tableName);
        List<Map<String, Object>> dataList = dataTransportService.selectData(tableName);

        String s = dataTransportService.exportData(datasourceName, tableName, columnNameList, dataList);
        System.out.println(s);
    }

    @Test
    void getDataLength() {
        String datasourceName = "xxl_job";
        MultiDatasourceThreadLocal.setDatasourceName(datasourceName);
        Long test1 = dataTransportService.getDataLength("xxl_job_log");
        System.out.println(test1);
    }

    @Test
    void archiveData() {
        ArchiveDataParam param = new ArchiveDataParam();
        param.setDatasourceName("datasource1");
        param.setTableName("mocktable");
        param.setTime(1);
        param.setTimeUnit(DataTransportTimeUnit.MINUTE);
        param.setDateColumn("update_date");
        param.setFileMaxSize("100M");
        dataTransportService.archiveData(param);
    }
}