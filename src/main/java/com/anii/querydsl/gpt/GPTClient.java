package com.anii.querydsl.gpt;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GPTClient {

    Flux<String> chatStream(Completion completion);

    Mono<String> chat(Completion completion);
}
