package com.anii.querydsl.common.utils;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class JSONUtils {

    private JSONUtils() {
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static void setGlobalMapper(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    /**
     * @param <T>   待转换的类型
     * @param json  带转换的json字符串
     * @param clazz 泛型推导帮助类
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }

        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }

    public static <T> String toJsonString(T object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessConstantEnum.JSON_PARSE_FAILED, e);
        }
    }
}
