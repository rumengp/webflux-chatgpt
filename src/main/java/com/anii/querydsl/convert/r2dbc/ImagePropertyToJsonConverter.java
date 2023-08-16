package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.common.utils.JSONUtils;
import com.anii.querydsl.entity.ChatImage;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public enum ImagePropertyToJsonConverter implements Converter<ChatImage.Property, Json> {

    /**
     * 单例模式
     */
    INSTANCE;

    @Override
    public Json convert(ChatImage.Property source) {
        return Json.of(JSONUtils.toJsonString(source));
    }
}
