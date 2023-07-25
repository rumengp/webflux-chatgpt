package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.chat.ChatCreateRequest;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import com.anii.querydsl.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChatHandler {

    private final IChatService chatService;

    @Bean("chatRouters")
    public RouterFunction<ServerResponse> functions() {
        return RouterFunctions.route()
                .path("/chat", router ->
                        router.POST("/", this::createNewChat)
                                .GET("/", this::findAll)
                                .GET("/{id}", this::findById)
                                .PUT("/{id}", this::updateById)
                                .DELETE("/{id}", this::deleteById)
                                .POST("/{id}/stream", this::chatStream)
                )
                .build();
    }

    private Mono<ServerResponse> chatStream(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> deleteById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatService.deleteById(id)
                .then(CommonResult.ok());
    }

    private Mono<ServerResponse> updateById(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatService.findById(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return RequestUtils.parse(request, ChatRoleQueryRequest.class)
                .flatMapMany(chatService::findAllByRole)
                .collectList()
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> createNewChat(ServerRequest request) {
        return RequestUtils.parse(request, ChatCreateRequest.class)
                .flatMap(chatService::createNewChat)
                .flatMap(CommonResult::ok);
    }
}
