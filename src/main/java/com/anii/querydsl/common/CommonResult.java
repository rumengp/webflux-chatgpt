package com.anii.querydsl.common;

import com.anii.querydsl.exception.BusinessException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public record CommonResult<T>(T data, String message, String code) {

    private static String SUCCESS_MESSAGE = "success";
    private static String SUCCESS_CODE = "0";

    private static String BAD_MESSAGE = "非法参数";
    private static String BAD_CODE = "10400";

    private static String ERROR_MESSAGE = "服务器异常";
    private static String ERROR_CODE = "10500";

    public static <T> Mono<ServerResponse> ok(Flux<T> data) {
        Flux<CommonResult<T>> mapped = data.map(d -> new CommonResult<>(d, SUCCESS_MESSAGE, SUCCESS_CODE));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapped, new ParameterizedTypeReference<CommonResult<T>>() {
                });
    }

    public static <T> Mono<ServerResponse> ok(Mono<T> data) {
        Mono<CommonResult<T>> mapped = data.map(d -> new CommonResult<>(d, SUCCESS_MESSAGE, SUCCESS_CODE));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapped, new ParameterizedTypeReference<CommonResult<T>>() {
                });
    }

    public static <T> Mono<ServerResponse> ok(T data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommonResult<>(data, SUCCESS_MESSAGE, SUCCESS_CODE));
    }

    public static Mono<ServerResponse> ok() {
        return ok("");
    }

    public static Mono<ServerResponse> bad() {
        return bad(BAD_MESSAGE);
    }

    public static Mono<ServerResponse> bad(String message) {
        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommonResult<>("", message, BAD_CODE));
    }

    public static Mono<ServerResponse> error() {
        return error(ERROR_MESSAGE, ERROR_CODE);
    }

    public static Mono<ServerResponse> error(Exception exception) {
        return error(exception.getMessage(), ERROR_CODE);
    }

    public static Mono<ServerResponse> error(BusinessException exception) {
        return error(exception.getMessage(), exception.getCode());
    }

    public static Mono<ServerResponse> error(String message) {
        return error(message, ERROR_CODE);
    }

    public static Mono<ServerResponse> error(String message, String code) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CommonResult<>("", message, code));
    }

}
