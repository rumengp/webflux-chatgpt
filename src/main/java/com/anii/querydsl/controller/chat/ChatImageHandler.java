package com.anii.querydsl.controller.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class ChatImageHandler {

    @Bean("chatImageRouters")
    public RouterFunction<ServerResponse> functions() {
        // return RouterFunctions.route()
        //         .path("/chat/image", router ->
        //                 router.POST("");
        //         )
        //         .build();
        return null;
    }
}
