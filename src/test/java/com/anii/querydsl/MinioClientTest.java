package com.anii.querydsl;

import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@SpringBootTest
public class MinioClientTest {

    @Autowired
    private MinioAsyncClient minioAsyncClient;

    @Test
    public void listBucketsTest() throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException {
        Mono.fromFuture(minioAsyncClient.listBuckets())
                .flatMapMany(Flux::fromIterable)
                .map(Bucket::name)
                .subscribe(System.out::println);
    }

    @Test
    public void createBucketTest() {
        Mono.fromFuture(() -> {
            try {
                return minioAsyncClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket("bucket-test")
                                .build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).subscribe();
    }

    @Test
    public void deleteBucketTest() {
        Mono.fromFuture(() -> {
            try {
                return minioAsyncClient.removeBucket(
                        RemoveBucketArgs.builder()
                                .bucket("bucket-test")
                                .build()
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).subscribe();
    }

    @Test
    public void uploadFileTest() {
        Mono.fromFuture(() -> {
                    try {
                        return minioAsyncClient.uploadObject(
                                UploadObjectArgs.builder()
                                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                        .bucket("gpt-images")
                                        .object("application.yamls")
                                        .filename("src/main/resources/application.yaml")
                                        .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(Objects::toString)
                .subscribe(System.out::println);
    }

    @Test
    public void downloadFileTest() {
        Mono.fromFuture(() -> {
                    try {
                        return minioAsyncClient.downloadObject(
                                DownloadObjectArgs.builder()
                                        .bucket("gpt-images")
                                        .object("application.yamls")
                                        .filename("src/main/resources/application2.yaml")
                                        .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(Objects::toString)
                .subscribe(System.out::println);
    }
}
