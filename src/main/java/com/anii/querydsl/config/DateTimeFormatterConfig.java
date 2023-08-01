package com.anii.querydsl.config;

import com.anii.querydsl.convert.web.StringToLocalDateConverter;
import com.anii.querydsl.convert.web.StringToLocalDateTimeConverter;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class DateTimeFormatterConfig {

    @Value("${spring.webflux.format.date-time:yyyy-MM-dd HH:mm:ss}")
    private String dateTimePattern;

    @Value("${spring.webflux.format.date:yyyy-MM-dd}")
    private String datePattern;

    // localDateTime 序列化器
    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    // localDateTime 反序列化器
    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    @Bean
    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(DateTimeFormatter.ofPattern(datePattern));
    }

    @Bean
    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(DateTimeFormatter.ofPattern(datePattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, localDateTimeSerializer());
            builder.serializerByType(LocalDate.class, localDateSerializer());
            builder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
            builder.deserializerByType(LocalDate.class, localDateDeserializer());
            builder.simpleDateFormat(dateTimePattern);
        };
    }

    @Bean
    public StringToLocalDateTimeConverter stringToLocalDateTimeConverter() {
        return new StringToLocalDateTimeConverter(DateTimeFormatter.ofPattern(dateTimePattern));
    }

    @Bean
    public StringToLocalDateConverter stringToLocalDateConverter() {
        return new StringToLocalDateConverter(DateTimeFormatter.ofPattern(datePattern));
    }
}
