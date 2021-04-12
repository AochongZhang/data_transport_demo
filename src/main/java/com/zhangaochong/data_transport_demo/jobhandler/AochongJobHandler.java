package com.zhangaochong.data_transport_demo.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhangaochong.data_transport_demo.service.DataTransportService;
import com.zhangaochong.data_transport_demo.util.StringParamsUtils;
import com.zhangaochong.data_transport_demo.vo.BackupDataParam;
import com.zhangaochong.data_transport_demo.vo.RecoverDataParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 17:36
 */
@Slf4j
@Component
public class AochongJobHandler {
    @Autowired
    private DataTransportService dataTransportService;

    @XxlJob("AochongJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        log.info("param={}", param);
        BackupDataParam jobParam = BackupDataParam.formMap(StringParamsUtils.parseParams(param));
        log.info("jobParam={}", jobParam);
        return ReturnT.SUCCESS;
    }

    @XxlJob("BackupDataJobHandler")
    public ReturnT<String > backupDataJobHandler(String param) throws Exception {
        log.info("[数据备份] 开始============================================");
        log.info("[数据备份] 参数={}", param);
        BackupDataParam params = BackupDataParam.formMap(StringParamsUtils.parseParams(param));
        int row = dataTransportService.backupData(params);
        log.info("[数据备份] 备份条数={}", row);
        log.info("[数据备份] 结束============================================");
        return ReturnT.SUCCESS;
    }

    @XxlJob("RecoverDataJobHandler")
    public ReturnT<String > recoverDataJobHandler(String param) throws Exception {
        log.info("[数据恢复] 开始============================================");
        log.info("[数据恢复] 参数={}", param);
        RecoverDataParam params = RecoverDataParam.formMap(StringParamsUtils.parseParams(param));
        int row = dataTransportService.recoverData(params);
        log.info("[数据备份] 恢复条数={}", row);
        log.info("[数据恢复] 结束============================================");
        return ReturnT.SUCCESS;
    }
}
