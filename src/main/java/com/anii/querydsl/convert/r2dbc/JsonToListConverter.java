package com.anii.querydsl.convert.r2dbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

@RequiredArgsConstructor
@ReadingConverter
public class JsonToListConverter implements Converter<String, List<String>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<String> convert(String source) {
        try {
            List<String> strings = objectMapper.readValue(source, new TypeReference<List<String>>() {
            });
            return strings;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
