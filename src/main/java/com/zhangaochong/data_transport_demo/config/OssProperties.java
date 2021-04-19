package com.zhangaochong.data_transport_demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Aochong Zhang
 * @date 2021-04-19 09:42
 */
@Data
@ConfigurationProperties(prefix = "oss")
@Component
public class OssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
