package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.service.IMinioService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MinioAsyncClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioServiceImpl implements IMinioService {

    private final MinioAsyncClient minioAsyncClient;

    @Override
    public Flux<String> listBuckets() {
        return Mono.fromFuture(() -> {
                    try {
                        return minioAsyncClient.listBuckets();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .flatMapMany(Flux::fromIterable)
                .map(Bucket::name);
    }

    @Override
    public Mono<Boolean> bucketExists(String bucketName) {
        return Mono.fromFuture(() -> {
            try {
                return minioAsyncClient.bucketExists(
                        BucketExistsArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Mono<Void> putObject(String bucketName, String objectName, InputStream is, Long size) {
        return Mono.fromFuture(() -> {
            try {
                return minioAsyncClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(is, size, -1)
                                .build()
                );
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new BusinessException(BusinessConstantEnum.MINIO_PUT_OBJECT_ERROR);
            }
        }).then();
    }

    @Override
    public Mono<InputStream> getObject(String bucketName, String objectName) {
        return Mono.fromFuture(() -> {
            try {
                return minioAsyncClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()

                );
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new BusinessException(BusinessConstantEnum.MINIO_GET_OBJECT_ERROR);
            }
        });
    }

}
