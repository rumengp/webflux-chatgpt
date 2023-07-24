package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.chat.ChatRoleCreateRequest;
import com.anii.querydsl.service.IChatRoleService;
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
public class ChatRoleHandler {

    private final IChatRoleService chatRoleService;

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
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatRoleService.deleteById(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> updateById(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatRoleService.findByIdAndUsername(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> createNewRole(ServerRequest request) {
        return RequestUtils.parse(request, ChatRoleCreateRequest.class)
                .flatMap(chatRoleService::saveChatRole)
                .flatMap(CommonResult::ok);
    }

}
