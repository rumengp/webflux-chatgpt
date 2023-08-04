package com.anii.querydsl.gpt;

import com.anii.querydsl.gpt.chat.Completion;
import com.anii.querydsl.gpt.chat.Response;
import com.anii.querydsl.gpt.exception.GPTErrorBody;
import com.anii.querydsl.gpt.exception.GPTException;
import com.anii.querydsl.gpt.image.ImageData;
import com.anii.querydsl.gpt.image.ImageRequest;
import com.anii.querydsl.gpt.image.ImageResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class DefaultGPTClient implements GPTClient {

    private static final String CHAT_PATH = "/v1/chat/completions";

    private static final String IMAGE_CREATE_PATH = "/v1/images/generations";

    private static final String FORMAT_B64_JSON = "b64_json";

    private static final String FORMAT_URL = "url";

    private final WebClient webClient;

    public DefaultGPTClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> chatStream(Completion completion) {
        completion.setStream(Boolean.TRUE);
        return webClient
                .post()
                .uri(CHAT_PATH)
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
                .uri(CHAT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(completion)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(GPTErrorBody.class)
                                .map(GPTException::ofResponse)
                )
                .bodyToMono(Response.class)
                .map(response -> response.choices().iterator().next().message().getContent());
    }

    @Override
    public Flux<String> createImageB64Json(ImageRequest request) {
        request.setResponseFormat(FORMAT_B64_JSON);
        return postImage(request)
                .map(ImageData::b64Json);
    }

    @Override
    public Flux<String> createImageUrl(ImageRequest request) {
        request.setResponseFormat(FORMAT_URL);
        return postImage(request)
                .map(ImageData::url);
    }

    private Flux<ImageData> postImage(ImageRequest request) {
        return webClient
                .post()
                .uri(IMAGE_CREATE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(GPTErrorBody.class)
                                .map(GPTException::ofResponse)
                )
                .bodyToMono(ImageResponse.class)
                .map(ImageResponse::data)
                .flatMapMany(Flux::fromIterable);
    }

}
