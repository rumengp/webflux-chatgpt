package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.dao.ChatImageRepository;
import com.anii.querydsl.entity.ChatImage;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.image.ImageRequest;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import com.anii.querydsl.service.IChatImageService;
import com.anii.querydsl.service.IMinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static com.anii.querydsl.mapper.ChatImageMapper.MAPPER;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatImageServiceImpl extends ServiceImpl<ChatImageRepository, ChatImage, Long> implements IChatImageService {

    @Value("${minio.gpt.bucket}")
    private String bucketName;

    @Value("${minio.gpt.image-prefix}")
    private String imagePrefix;

    private final GPTClient gptClient;

    private final IMinioService minioService;

    @Override
    public Flux<String> createImage(ChatImageCreateRequest request, String username) {

        ImageRequest imageRequest = MAPPER.toImageRequest(request);
        imageRequest.setUser(username);

        Flux<String> imageB64Json = gptClient.createImageB64Json(imageRequest).cache();

        String format = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Stream<String> objectNames = Stream.generate(
                        () -> RandomStringUtils.random(16, true, true))
                .limit(request.n())
                .map(name -> String.join("/", imagePrefix, format, name + ".png.base64"));
        Flux<String> objectNamesFlux = Flux.fromStream(objectNames).cache();

        // 将结果存入minio
        imageB64Json.zipWith(objectNamesFlux, this::saveImageWithObjectName)
                .onErrorMap(ex -> new BusinessException(BusinessConstantEnum.MINIO_PUT_OBJECT_ERROR, ex))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        // 存入数据库
        Flux<String> saveDb = Mono.just(request)
                .zipWith(objectNamesFlux.collectList(), MAPPER::toDo)
                .flatMap(repository::save)
                .thenMany(Flux.empty());

        return Flux.concat(imageB64Json, saveDb);
    }

    private Mono<Void> saveImageWithObjectName(String imageBase64, String objectName) {
        byte[] bytes = imageBase64.getBytes(StandardCharsets.UTF_8);
        return Mono.using(() -> new ByteArrayInputStream(bytes),
                is -> minioService.putObject(bucketName, objectName, is, Integer.toUnsignedLong(bytes.length)),
                IOUtils::closeQuietly
        ).then();
    }
}
