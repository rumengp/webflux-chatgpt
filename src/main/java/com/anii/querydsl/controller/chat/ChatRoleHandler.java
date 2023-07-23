package com.anii.querydsl.controller.chat;

import com.anii.querydsl.service.IChatRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public record ChatRoleHandler(IChatRoleService chatRoleService) {

    @Bean("chatRoleRouters")
    public RouterFunction<ServerResponse> functions() {
        return RouterFunctions.route()
                .path("/chat/role", router ->
                        router.POST("/", this::createNewRole)
                                .GET("/", this::findAll)
                                .GET("/{id}", this::findById)
                                .PUT("/{id}", this::updateById)
                                .DELETE("/{id}", this::deleteById)
                )
                .build();
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

    private Mono<ServerResponse> createNewRole(ServerRequest request) {
        return null;
    }

}
