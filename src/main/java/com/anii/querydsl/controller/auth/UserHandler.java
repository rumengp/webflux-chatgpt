package com.anii.querydsl.controller.auth;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.AuthRequest;
import com.anii.querydsl.request.UserRegisterReq;
import com.anii.querydsl.service.UserService;
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
public class UserHandler {

    private final UserService userService;

    @Bean("userRouters")
    public RouterFunction<ServerResponse> functions() {
        return RouterFunctions.route()
                .path("/auth", route ->
                        route.GET("/", this::findAll)
                                .POST("/register", this::register)
                                .POST("/login", this::login)
                )
                .build();
    }

    private Mono<ServerResponse> login(ServerRequest request) {
        return RequestUtils.parseAndValid(request, AuthRequest.class)
                .flatMap(userService::login)
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> findAll(ServerRequest request) {
        return userService.findAll()
                .flatMap(CommonResult::ok);
    }

    private Mono<ServerResponse> register(ServerRequest request) {
        return RequestUtils.parse(request, UserRegisterReq.class)
                .flatMap(userService::register)
                .flatMap(CommonResult::ok);
    }

}
