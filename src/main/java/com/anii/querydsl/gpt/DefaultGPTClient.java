package com.anii.querydsl.gpt;

import com.anii.querydsl.gpt.exception.GPTErrorBody;
import com.anii.querydsl.gpt.exception.GPTException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Getter
public class DefaultGPTClient implements GPTClient {
    private final String path = "/v1/chat/completions";
    private final WebClient webClient;

    public DefaultGPTClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> chatStream(Completion completion) {
        completion.setStream(Boolean.TRUE);
        return webClient
                .post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completion)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(GPTErrorBody.class)
                                .map(GPTException::ofResponse)
                )
                .bodyToFlux(Response.class)
                .takeWhile(c -> c.choices().get(0).finishReason() == null)
                .map(c -> c.choices().get(0).delta().getContent())
                .filter(StringUtils::isNotBlank);
    }

    @Override
    public Mono<String> chat(Completion completion) {
        return webClient
                .post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completion)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(Response.class)
                .map(response -> response.choices().iterator().next().message().getContent());
    }
}
