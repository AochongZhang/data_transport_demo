package com.zhangaochong.data_transport_demo.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhangaochong.data_transport_demo.util.StringParamsUtils;
import com.zhangaochong.data_transport_demo.vo.DataTransportJobParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Aochong Zhang
 * @date 2021-04-09 17:36
 */
@Slf4j
@Component
public class AochongJobHandler {

    @XxlJob("AochongJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        log.info("param={}", param);
        DataTransportJobParam jobParam = DataTransportJobParam.formMap(StringParamsUtils.parseParams(param));
        log.info("jobParam={}", jobParam);
        return ReturnT.SUCCESS;
    }
}
