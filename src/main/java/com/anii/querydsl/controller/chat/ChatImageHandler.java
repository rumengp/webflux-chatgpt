package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import com.anii.querydsl.service.IChatImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatImageHandler {

    private final IChatImageService chatImageService;

    @Bean("chatImageRouters")
    public RouterFunction<ServerResponse> functions() {
        return RouterFunctions.route()
                .path("/chat/image", router ->
                        router.POST("/", this::createImagePrompt)
                                .GET("/{id}", this::getImageList)
                )
                .build();
    }

    private Mono<ServerResponse> getImageList(ServerRequest serverRequest) {
        return null;
    }

    private Mono<ServerResponse> createImagePrompt(ServerRequest serverRequest) {
        Flux<String> stringFlux = serverRequest.bodyToMono(ChatImageCreateRequest.class)
                .flatMapMany(chatImageService::createImage);

        return CommonResult.ok(stringFlux);
    }
}
