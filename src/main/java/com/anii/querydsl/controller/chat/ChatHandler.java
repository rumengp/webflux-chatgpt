package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.chat.ChatCreateRequest;
import com.anii.querydsl.request.chat.ChatMessageRequest;
import com.anii.querydsl.request.chat.ChatUpdateRequest;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import com.anii.querydsl.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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

        Long id = Long.valueOf(request.pathVariable("id"));
        Flux<String> messages = RequestUtils.parse(request, ChatMessageRequest.class)
                .flatMapMany(req -> chatService.chatStream(id, req));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(messages, String.class);
    }

    private Mono<ServerResponse> deleteById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return chatService.deleteById(id)
                .then(CommonResult.ok());
    }

    private Mono<ServerResponse> updateById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return RequestUtils.parseAndValid(request, ChatUpdateRequest.class)
                .flatMap(req -> chatService.updateById(id, req))
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> createNewChat(ServerRequest request) {
        return RequestUtils.parse(request, ChatCreateRequest.class)
                .flatMap(chatService::createNewChat)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return chatService.findById(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return RequestUtils.parse(request, ChatRoleQueryRequest.class)
                .flatMapMany(chatService::findAllByRole)
                .collectList()
                .flatMap(CommonResult::ok);
    }
}
