package com.zhangaochong.data_transport_demo.util;

import com.zhangaochong.data_transport_demo.config.DataTransportProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MySqlDumpUtilsTest {
    @Autowired
    private DataTransportProperties dataTransportProperties;

    @Test
    void test() {
        Map<String, DataSourceProperties> multiDatasource = dataTransportProperties.getMultiDatasource();
        DataSourceProperties datasource1 = multiDatasource.get("datasource1");
        String command = MySqlDumpUtils.buildCommand(datasource1, "test1", "./exportdata/test11.sql");
        CommandUtils.execCommand(command);
    }
}