package com.anii.querydsl.controller.chat;

import com.anii.querydsl.service.IChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record ChatHandler(IChatService chatService) {

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
        return null;
    }

    private Mono<ServerResponse> updateById(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> createNewChat(ServerRequest request) {
        return null;
    }
}