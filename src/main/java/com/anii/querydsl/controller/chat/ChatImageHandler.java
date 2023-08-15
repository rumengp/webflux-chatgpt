package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.UserContextHolder;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.chat.image.ChatImageCreateRequest;
import com.anii.querydsl.request.chat.image.ChatImageRolloutDTO;
import com.anii.querydsl.service.IChatImageService;
import com.anii.querydsl.vo.ChatImageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

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
                                .POST("/rollout", this::getImageList)
                )
                .build();
    }

    private Mono<ServerResponse> getImageList(ServerRequest serverRequest) {
        Flux<ChatImageVo> objectFlux = RequestUtils.parse(serverRequest, ChatImageRolloutDTO.class)
                .flatMapMany(chatImageService::rolloutImageList);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(objectFlux, ChatImageVo.class);
    }

    private Mono<ServerResponse> createImagePrompt(ServerRequest serverRequest) {
        Flux<String> stringFlux = serverRequest.bodyToMono(ChatImageCreateRequest.class)
                .zipWith(UserContextHolder.getUsername(), chatImageService::createImage)
                .flatMapMany(Function.identity());

        return CommonResult.ok(stringFlux);
    }
}
