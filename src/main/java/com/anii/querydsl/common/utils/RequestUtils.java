package com.anii.querydsl.common.utils;

import com.anii.querydsl.exception.ValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Slf4j
public class RequestUtils {

    private static ObjectMapper objectMapper;
    private static Validator validator;

    @Autowired
    public void setValidator(Validator val) {
        validator = val;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public static <T> Mono<T> parse(ServerRequest serverRequest, Class<T> clazz) {

        HttpMethod method = serverRequest.method();
        if (HttpMethod.GET.equals(method)) {
            return GET(serverRequest, clazz);
        }

        return POST(serverRequest, clazz);
    }

    private static <T> Mono<T> GET(ServerRequest request, Class<T> clazz) {
        Map<String, String> queryParams = request.queryParams().toSingleValueMap();
        Map<String, String> pathVariables = request.pathVariables();
        try {
            String json = objectMapper.writeValueAsString(queryParams);
            return Mono.just(objectMapper.readValue(json, clazz))
                    .zipWith(Mono.just(pathVariables), (t, map) -> {
                        BeanUtils.copyProperties(map, t);
                        return t;
                    })
                    .doOnNext(RequestUtils::valid);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert query parameters to class", e);
        }
    }

    /**
     * 将请求中的请求体和路径变量都转换到一个对象中
     */
    private static <T> Mono<T> POST(ServerRequest request, Class<T> clazz) {
        Map<String, String> pathVariables = request.pathVariables();
        return request.bodyToMono(clazz)
                .zipWith(Mono.just(pathVariables), (t, map) -> {
                    BeanUtils.copyProperties(map, t);
                    return t;
                })
                .doOnNext(RequestUtils::valid);
    }

    public static <T> void valid(@NonNull T object) {
        log.info("valid {}", object);
        Errors errors = new BeanPropertyBindingResult(object, object.getClass().getName());
        ValidationUtils.invokeValidator(validator, object, errors);
        if (errors.hasErrors()) {
            throw new ValidException(errors);
        }
    }
}
