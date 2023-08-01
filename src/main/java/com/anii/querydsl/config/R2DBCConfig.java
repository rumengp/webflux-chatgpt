package com.anii.querydsl.config;

import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.convert.r2dbc.JsonToListConverter;
import com.anii.querydsl.convert.r2dbc.ListToJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.NamingStrategy;

import java.util.List;

@Configuration
@EnableR2dbcAuditing
public class R2DBCConfig {

    @Bean
    ReactiveAuditorAware<String> auditorAware() {
        return () -> UserContextHolder.getUsername();
    }

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        var dialect = DialectResolver.getDialect(connectionFactory);
        var converters = List.of(
                JsonToListConverter.INSTANCE,
                ListToJsonConverter.INSTANCE
        );
        return R2dbcCustomConversions.of(dialect, converters);
    }

    @Bean
    public NamingStrategy defaultNamingStrategy() {
        return new DefaultNamingStrategy();
    }
}
