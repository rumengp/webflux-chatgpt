package com.anii.querydsl;

import com.anii.querydsl.gpt.image.ImageResponse;
import com.anii.querydsl.gpt.image.ImageData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.minio.*;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Bucket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@SpringBootTest
public class MinioClientTest {

    @Autowired
    private MinioAsyncClient minioAsyncClient;

    @Autowired
    @Qualifier("gptWebClient")
    private WebClient client;

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
                                        .object("1691037278708.png")
                                        .filename("src/main/resources/image.png")
                                        .build());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(Objects::toString)
                .subscribe(System.out::println);
    }

    @Test
    public void downPictureTest() throws InterruptedException {
        String url = "https://oaidalleapiprodscus.blob.core.windows.net/private/org-Mx6JBgZVSZ1E7hfM940jmnjK/user-WbIs8j3Tx7BFgH9zFWKipAex/img-kZQLBri6XGOMsItfD0NRScV3.png?st=2023-08-03T00%3A35%3A17Z&se=2023-08-03T02%3A35%3A17Z&sp=r&sv=2021-08-06&sr=b&rscd=inline&rsct=image/png&skoid=6aaadede-4fb3-4698-a8f6-684d7786b067&sktid=a48cca56-e6da-484e-a814-9c849652bcb3&skt=2023-08-02T17%3A20%3A01Z&ske=2023-08-03T17%3A20%3A01Z&sks=b&skv=2021-08-06&sig=XCsa9iNwjZC/Fy/KdRSuSgjZ3y5Jo3ywRorK6yx5N/g%3D";
        String decodeUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);

        Flux<DataBuffer> flux = client.get()
                .uri(decodeUrl)
                // .accept(MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG)
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        DataBufferUtils.write(flux, Path.of("src/main/resources/imgage.png"))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        Thread.currentThread().join(5000);
    }

    @Test
    public void decodeBase64Image() throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/http/2023-08-03T102121.200.json"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        ImageResponse response = objectMapper.readValue(bytes, ImageResponse.class);
        List<String> images = response.data().stream().map(ImageData::b64Json).toList();

        Base64.Decoder decoder = Base64.getDecoder();

        for (String image : images) {
            byte[] decode = decoder.decode(image);
            // 写入文件
            String filename = "src/main/resources/http/" + System.currentTimeMillis() + ".png";
            try (FileChannel channel = FileChannel.open(Path.of(filename), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                ByteBuffer buffer = ByteBuffer.wrap(decode);
                channel.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void gptCreateImageTest() throws InterruptedException {

        String body = """ 
                  {
                  "prompt": "flowers",
                  "n": 2,
                  "size": "256x256",
                  "response_format": "b64_json"
                }""";

        Base64.Decoder decoder = Base64.getDecoder();
        client.post()
                .uri("/v1/images/generations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ImageResponse.class)
                .checkpoint()
                .map(ImageResponse::data)
                .flatMapMany(Flux::fromIterable)
                .map(ImageData::b64Json)
                .map(decoder::decode)
                .checkpoint()
                .flatMap(bytes -> {
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                    return Mono.fromFuture(() -> {
                                try {
                                    return minioAsyncClient.putObject(
                                            PutObjectArgs.builder()
                                                    .object(System.currentTimeMillis() + ".png")
                                                    .bucket("gpt-images")
                                                    .stream(inputStream, bytes.length, -1)
                                                    .build());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                    );
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
        ByteBuffer.wrap(new byte[0]);

        Thread.currentThread().join();

    }
}
