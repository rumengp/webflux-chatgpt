package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.entity.ChatImage;
import io.r2dbc.postgresql.codec.Json;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public enum ImagePropertyToJsonConverter implements Converter<ChatImage.Property, Json> {

    INSTANCE;

    @Override
    public Json convert(ChatImage.Property source) {
        byte[] serialize = SerializationUtils.serialize(source);
        return Json.of(serialize);
    }
}
