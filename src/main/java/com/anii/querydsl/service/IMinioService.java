package com.anii.querydsl.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public interface IMinioService {

    Flux<String> listBuckets();

    Mono<Boolean> bucketExists(String bucketName);

    Mono<String> putObject(String bucketName, String objectName, InputStream is, Long size);

    Mono<String> putObject(String bucketName, String objectName, byte[] bytes);

    Mono<InputStream> getObject(String bucketName, String objectName);

    Mono<String> getB64Image(String bucketName, String objectName);
}
