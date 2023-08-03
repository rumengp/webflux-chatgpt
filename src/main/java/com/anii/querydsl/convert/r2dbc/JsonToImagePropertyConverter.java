package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.common.utils.JSONUtils;
import com.anii.querydsl.entity.ChatImage;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public enum JsonToImagePropertyConverter implements Converter<Json, ChatImage.Property> {

    INSTANCE;

    @Override
    public ChatImage.Property convert(Json source) {
        return JSONUtils.parseObject(source.asString());
    }
}
