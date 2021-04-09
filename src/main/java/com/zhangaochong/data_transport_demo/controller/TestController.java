package com.zhangaochong.data_transport_demo.controller;

import com.zhangaochong.data_transport_demo.config.MultiDatasourceThreadLocal;
import com.zhangaochong.data_transport_demo.entity.User;
import com.zhangaochong.data_transport_demo.service.DataTransportService;
import com.zhangaochong.data_transport_demo.service.UserService;
import com.zhangaochong.data_transport_demo.vo.DataTransportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Aochong Zhang
 * @date 2021-04-08 14:52
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private DataTransportService dataTransportService;

    @PostMapping("/transport")
    public String transport(@RequestBody DataTransportVO dataTransportVO) {
        log.info("请求参数：{}", dataTransportVO);
        // 根据表名判断表是否存在，同时可以防SQL注入
        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getSourceDatasource());
        boolean sourceTableExist = dataTransportService.isTableExist(dataTransportVO.getSourceTable());
        MultiDatasourceThreadLocal.removeDatasourceName();
        if (!sourceTableExist) {
            throw new RuntimeException(dataTransportVO.getSourceTable() + "表不存在");
        }

        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getTargetDatasource());
        boolean targetTableExist = dataTransportService.isTableExist(dataTransportVO.getTargetTable());
        MultiDatasourceThreadLocal.removeDatasourceName();
        if (!targetTableExist) {
            throw new RuntimeException(dataTransportVO.getTargetTable() + "表不存在");
        }

        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getSourceDatasource());
        List<User> userList = userService.getUserList(dataTransportVO.getSourceTable());
        MultiDatasourceThreadLocal.removeDatasourceName();

        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getTargetDatasource());
        List<User> userList2 = userService.getUserList(dataTransportVO.getTargetTable());
        MultiDatasourceThreadLocal.removeDatasourceName();

        log.info("{}", userList);
        log.info("{}", userList2);

        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getSourceDatasource());
        userService.update(dataTransportVO.getSourceTable(), false);
        MultiDatasourceThreadLocal.removeDatasourceName();

        MultiDatasourceThreadLocal.setDatasourceName(dataTransportVO.getTargetDatasource());
        userService.update(dataTransportVO.getTargetTable(), true);
        MultiDatasourceThreadLocal.removeDatasourceName();

        return "ok";
    }
}
