package com.anii.querydsl.common.utils;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public final class JSONUtils {

    private static ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    public void setGlobalMapper(ObjectMapper objectMapper) {
        MAPPER = objectMapper;
    }

    /**
     * @param <T>   待转换的类型
     * @param json  带转换的json字符串
     * @param clazz 泛型推导帮助类
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }

        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> String toJsonString(T object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }

        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }
}
