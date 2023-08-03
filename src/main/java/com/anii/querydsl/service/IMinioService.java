package com.anii.querydsl.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface IMinioService {

    Flux<String> listBuckets();

    Mono<Boolean> bucketExists(String bucketName);

    Mono<Void> putObject(String bucketName, String objectName, InputStream is, Long size);

    Mono<InputStream> getObject(String bucketName, String objectName);
}
