package com.zhangaochong.data_transport_demo.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhangaochong.data_transport_demo.service.DataTransportService;
import com.zhangaochong.data_transport_demo.util.StringParamsUtils;
import com.zhangaochong.data_transport_demo.vo.ArchiveDataParam;
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
public class DataTransportJobHandler {
    @Autowired
    private DataTransportService dataTransportService;

    @XxlJob("BackupDataJobHandler")
    public ReturnT<String > backupDataJobHandler(String param) throws Exception {
        BackupDataParam params = BackupDataParam.formMap(StringParamsUtils.parseParams(param));
        int row = dataTransportService.backupData(params);
        return ReturnT.SUCCESS;
    }

    @XxlJob("RecoverDataJobHandler")
    public ReturnT<String > recoverDataJobHandler(String param) throws Exception {
        RecoverDataParam params = RecoverDataParam.formMap(StringParamsUtils.parseParams(param));
        int row = dataTransportService.recoverData(params);
        return ReturnT.SUCCESS;
    }

    @XxlJob("ArchiveDataJobHandler")
    public ReturnT<String > archiveDataJobHandler(String param) throws Exception {
        ArchiveDataParam params = ArchiveDataParam.formMap(StringParamsUtils.parseParams(param));
        int row = dataTransportService.archiveData(params);
        return ReturnT.SUCCESS;
    }
}
