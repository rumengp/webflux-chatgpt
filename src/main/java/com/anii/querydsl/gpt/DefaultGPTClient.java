package com.anii.querydsl.gpt;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultGPTClient implements GPTClient {

    private final String path = "/v1/chat/completions";

    private final String token;
    private final WebClient webClient;

    @Override
    public Flux<String> chatStream(Completion completion) {

        if (!completion.stream()) {
            throw new IllegalArgumentException("completion stream must be true");
        }

        return webClient
                .post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completion)
                .retrieve()
                .bodyToFlux(Response.class)
                .takeWhile(c -> c.choices().get(0).finishReason() == null)
                .map(c -> c.choices().get(0).delta().content())
                .filter(StringUtils::isNotBlank);
    }

    @Override
    public Mono<String> chat(Completion completion) {
        if (completion.stream()) {
            throw new IllegalArgumentException("completion stream must be false");
        }

        return webClient
                .post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completion)
                .retrieve()
                .bodyToMono(Response.class)
                .map(response -> response.choices().iterator().next().message().content());
    }
}
