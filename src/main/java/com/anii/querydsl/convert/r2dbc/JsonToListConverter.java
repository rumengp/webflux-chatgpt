package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.common.utils.JSONUtils;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.util.List;

@ReadingConverter
public enum JsonToListConverter implements Converter<Json, List<String>> {

    INSTANCE;

    @Override
    @NonNull
    public List<String> convert(Json source) {
        return JSONUtils.parseList(source.asString(), String.class);
    }
}
