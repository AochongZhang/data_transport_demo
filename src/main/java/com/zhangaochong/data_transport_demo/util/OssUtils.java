package com.zhangaochong.data_transport_demo.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.zhangaochong.data_transport_demo.config.OssProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author Aochong Zhang
 * @date 2021-04-19 09:37
 */
@Slf4j
public abstract class OssUtils {
    public static PutObjectResult upload(OssProperties ossProperties, File file, String uploadPath) {
        OSS oss = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret());
        String uploadFileName = uploadPath + file.getName();
        PutObjectResult putObjectResult = oss.putObject(ossProperties.getBucketName(), uploadFileName, file);
        log.info("[上传到oss] uploadFileName={}", uploadFileName);
        oss.shutdown();
        return putObjectResult;
    }
}
