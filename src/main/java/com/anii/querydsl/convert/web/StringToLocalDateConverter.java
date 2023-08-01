package com.anii.querydsl.convert.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    @Override
    public LocalDate convert(String source) {
        return LocalDate.parse(source, formatter);
    }
}
