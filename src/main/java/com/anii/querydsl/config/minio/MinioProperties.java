package com.anii.querydsl.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String endpoint;

    private String region;
}
