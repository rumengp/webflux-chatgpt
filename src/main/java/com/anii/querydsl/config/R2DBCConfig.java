package com.anii.querydsl.config;

import com.anii.querydsl.convert.JsonToListConverter;
import com.anii.querydsl.convert.ListToJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;

import java.util.List;

@Configuration
public class R2DBCConfig {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        var dialect = DialectResolver.getDialect(connectionFactory);
        var converters = List.of(
                new ListToJsonConverter(objectMapper),
                new JsonToListConverter(objectMapper)
        );
        return R2dbcCustomConversions.of(dialect, converters);
    }
}
