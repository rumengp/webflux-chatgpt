package com.anii.querydsl.convert.r2dbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.util.List;

@WritingConverter
@RequiredArgsConstructor
public class ListToJsonConverter implements Converter<List<String>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convert(List<String> source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
