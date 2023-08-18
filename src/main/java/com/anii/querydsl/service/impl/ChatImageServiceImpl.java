package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.common.UserContextHolder;
import com.anii.querydsl.dao.ChatImageRepository;
import com.anii.querydsl.entity.ChatImage;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.image.ImageRequest;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import com.anii.querydsl.request.chat.image.ChatImageRolloutDTO;
import com.anii.querydsl.service.IChatImageService;
import com.anii.querydsl.service.IMinioService;
import com.anii.querydsl.vo.ChatImageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;
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
        imageB64Json
                .map(String::getBytes)
                .zipWith(objectNamesFlux, (bytes, objectName) -> minioService.putObject(bucketName, objectName, bytes))
                .flatMap(Function.identity()) // 对于Flux<Mono<String>>返回值，按照reactor的原理分析，需要调用flatMap采用正常执行
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

    @Override
    public Flux<ChatImageVo> rolloutImageList(ChatImageRolloutDTO request) {
        // 按照上一个图片的id进行查找
        Flux<ChatImage> images = UserContextHolder.getUsername()
                .flatMapMany(username -> repository.findAllRollout(username, request.preImageId(), request.num()))
                .cache();

        // 按照查出的信息从minio拉取数据
        return images.flatMap(image -> {
            Flux<ChatImageVo> prompt = Flux.just(image.getPrompt())
                    .map(objectName -> ChatImageVo.builder()
                            .id(image.getId())
                            .type(ChatImageVo.ChatImageVoTypeEnum.PROMPT)
                            .content(image.getPrompt())
                            .build()
                    );
            Stream<String> imageObject = Stream.of(image.getImageObject(), image.getMaskObject())
                    .filter(StringUtils::isNotBlank);
            Flux<ChatImageVo> userImages = Flux.fromStream(imageObject)
                    .filter(StringUtils::isNotBlank)
                    .flatMap(objectName -> minioService.getB64Image(bucketName, objectName))
                    .map(content ->
                            ChatImageVo.builder()
                                    .id(image.getId())
                                    .type(ChatImageVo.ChatImageVoTypeEnum.USER_IMAGE)
                                    .content(content)
                                    .build()
                    )
                    .onErrorResume(e -> Mono.defer(() -> Mono.just(new ChatImageVo(0L, null, e.getMessage()))));

            Flux<ChatImageVo> respImages = Flux.fromIterable(image.getRespObject())
                    .flatMap(objectName -> minioService.getB64Image(bucketName, objectName))
                    .map(content ->
                            ChatImageVo.builder()
                                    .id(image.getId())
                                    .type(ChatImageVo.ChatImageVoTypeEnum.RESP_IMAGE)
                                    .content(content)
                                    .build()
                    );

            return Flux.concat(prompt, userImages, respImages);
        });
    }
}
