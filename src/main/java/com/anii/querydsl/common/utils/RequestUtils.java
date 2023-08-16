package com.anii.querydsl.common.utils;

import com.anii.querydsl.exception.ValidException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public abstract class RequestUtils {

    private RequestUtils() {
    }

    private static Validator validator;

    public static void setGlobalValidator(Validator val) {
        validator = val;
    }

    public static <T> Mono<T> parse(ServerRequest serverRequest, Class<T> clazz) {
        return doParseAndValid(serverRequest, clazz, false);
    }

    public static <T> Mono<T> parseAndValid(ServerRequest serverRequest, Class<T> clazz, Class... groups) {
        return doParseAndValid(serverRequest, clazz, true, groups);
    }

    private static <T> Mono<T> doParseAndValid(ServerRequest serverRequest, Class<T> clazz, boolean needValid, Class... groups) {
        HttpMethod method = serverRequest.method();
        if (HttpMethod.GET.equals(method)) {
            return GET(serverRequest, clazz, needValid, groups);
        }

        return POST(serverRequest, clazz, needValid, groups);
    }

    private static <T> Mono<T> GET(ServerRequest request, Class<T> clazz, boolean needValid, Class... groups) {
        Map<String, String> queryParams = request.queryParams().toSingleValueMap();
        try {
            String json = JSONUtils.toJsonString(queryParams);
            Mono<T> mono = Mono.just(JSONUtils.parseObject(json, clazz));
            if (needValid) {
                mono = mono.doOnNext(t -> valid(t, groups));
            }
            return mono;

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert query parameters to class", e);
        }
    }

    /**
     * 将请求中的请求体和路径变量都转换到一个对象中
     */
    private static <T> Mono<T> POST(ServerRequest request, Class<T> clazz, boolean needValid, Class... groups) {
        Mono<T> mono = request.bodyToMono(clazz);
        if (needValid) {
            mono = mono.doOnNext(t -> valid(t, groups));
        }
        return mono;

    }

    public static <T> void valid(@NonNull T object, Class... groups) {
        log.info("valid {}", object);
        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        ValidationUtils.invokeValidator(validator, object, errors, groups);
        if (errors.hasErrors()) {
            throw new ValidException(errors);
        }
    }
}
