package com.zhangaochong.data_transport_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhangaochong.data_transport_demo.dao")
public class DataTransportDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataTransportDemoApplication.class, args);
    }

}
