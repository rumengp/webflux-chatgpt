package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.common.utils.JSONUtils;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;

@WritingConverter
public enum ListToJsonConverter implements Converter<List, Json> {

    INSTANCE;

    @Override
    public Json convert(List source) {
        return Json.of(JSONUtils.toJsonString(source));
    }
}
