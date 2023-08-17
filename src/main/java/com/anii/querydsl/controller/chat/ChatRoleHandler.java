package com.anii.querydsl.controller.chat;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.chat.role.ChatRoleCreateRequest;
import com.anii.querydsl.service.IChatRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

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
                                .path("/{id}", c ->
                                        c.GET("", this::findById)
                                                .PUT("", this::updateById)
                                                .DELETE("", this::deleteById))
                )
                .build();
    }

    private Mono<ServerResponse> deleteById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatRoleService.deleteById(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> updateById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return Mono.justOrEmpty(id)
                .zipWith(RequestUtils.parseAndValid(request, ChatRoleCreateRequest.class),
                        chatRoleService::updateById
                )
                .flatMap(Function.identity())
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return chatRoleService.findById(id)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return chatRoleService.findAll()
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> createNewRole(ServerRequest request) {
        return RequestUtils.parseAndValid(request, ChatRoleCreateRequest.class)
                .flatMap(chatRoleService::saveChatRole)
                .flatMap(CommonResult::ok);
    }

}
