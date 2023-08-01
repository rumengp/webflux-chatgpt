package com.anii.querydsl.convert.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private final DateTimeFormatter formatter;

    @Override
    public LocalDateTime convert(String source) {
        // 2020-01-01 00:00:00
        String dateTimeStr = source + switch (source.length()) {
            case 10 -> " 00:00:00";
            case 13 -> ":00:00";
            case 16 -> ":00";
            default -> "";
        };
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
