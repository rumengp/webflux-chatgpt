package com.anii.querydsl.convert.r2dbc;

import com.anii.querydsl.common.utils.function.FunctionUtils;
import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.NonNull;

import java.util.List;

@WritingConverter
public enum ListToJsonConverter implements Converter<List, Json> {

    INSTANCE;

    @Override
    @NonNull
    public Json convert(List source) {
        Object collect = source.stream()
                .collect(FunctionUtils.jsonJoining());

        return Json.of(collect.toString());
    }
}
