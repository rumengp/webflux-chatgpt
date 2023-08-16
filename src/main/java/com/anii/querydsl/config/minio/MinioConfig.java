package com.anii.querydsl.config.minio;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioAsyncClient minioAsyncClient(MinioProperties properties) {
        return MinioAsyncClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint())
                .build();
    }

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        return MinioClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint())
                .build();
    }
}
