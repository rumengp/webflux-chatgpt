package com.anii.querydsl.controller;

import com.anii.querydsl.common.CommonResult;
import com.anii.querydsl.common.utils.RequestUtils;
import com.anii.querydsl.request.PersonPageRequest;
import com.anii.querydsl.service.PersonService;
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
public class PersonHandler {

    private final PersonService personService;

    @Bean("personRouters")
    public RouterFunction<ServerResponse> functions() {
        return RouterFunctions.route()
                .path("/person", router ->
                        router.GET("/", this::findAll)
                )
                .build();
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return RequestUtils.parse(request, PersonPageRequest.class)
                .flatMap(personService::pagePerson)
                .flatMap(CommonResult::ok);
    }

}
