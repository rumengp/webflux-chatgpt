package com.anii.querydsl.config.minio;

import io.minio.MinioAsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioAsyncClient minioAsyncClient(MinioProperties properties) {
        MinioAsyncClient asyncClient = MinioAsyncClient.builder()
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .endpoint(properties.getEndpoint())
                .build();
        return asyncClient;
    }
}
