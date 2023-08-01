package com.anii.querydsl.convert.r2dbc;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ReadingConverter
public enum JsonToListConverter implements Converter<Json, List> {

    INSTANCE;

    @Override
    @NonNull
    public List convert(Json source) {
        String string = source.asString();
        if (Objects.isNull(string) || string.isEmpty()) {
            return Collections.emptyList();
        }
        String replaced = string.replaceAll("[\\[\\]\s\"]", "");
        String[] split = replaced.split(",");
        return Arrays.asList(split);
    }
}
