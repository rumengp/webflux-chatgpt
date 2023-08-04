package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.dao.ChatImageRepository;
import com.anii.querydsl.entity.ChatImage;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.image.ImageRequest;
import com.anii.querydsl.mapper.ChatImageMapper;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import com.anii.querydsl.service.IChatImageService;
import com.anii.querydsl.service.IMinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

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
    public Flux<String> createImage(ChatImageCreateRequest request) {

        ImageRequest imageRequest = ChatImageMapper.MAPPER.toImageRequest(request);

        Flux<String> imageB64Json = gptClient.createImageB64Json(imageRequest).cache();

        String format = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Stream<String> objectNames = Stream.generate(
                        () -> RandomStringUtils.random(16, true, true))
                .limit(request.n())
                .map(name -> String.join("/", imagePrefix, format, name + ".png.base64"));
        Flux<String> objectNamesFlux = Flux.fromStream(objectNames).cache();

        // 将结果存入minio
        Flux.zip(imageB64Json, objectNamesFlux)
                .flatMap(t -> {
                    byte[] bytes = t.getT1().getBytes();
                    try (InputStream is = new ByteArrayInputStream(bytes)) {
                        return minioService.putObject(bucketName, t.getT2(), is, Integer.toUnsignedLong(bytes.length));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> new BusinessException(BusinessConstantEnum.MINIO_PUT_OBJECT_ERROR, ex))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();

        // 存入数据库
        Flux<String> saveDb = objectNamesFlux.collectList()
                .zipWith(Mono.just(request), (names, req) -> ChatImageMapper.MAPPER.toDo(request, names))
                .flatMap(repository::save)
                .thenMany(Flux.empty());

        return Flux.concat(imageB64Json, saveDb);
    }
}
