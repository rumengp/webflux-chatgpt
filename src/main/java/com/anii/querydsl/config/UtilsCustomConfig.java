package com.anii.querydsl.config;

import com.anii.querydsl.common.UtilsCustomizer;
import com.anii.querydsl.common.utils.JSONUtils;
import com.anii.querydsl.common.utils.RequestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

import java.util.Optional;

@Configuration
public class UtilsCustomConfig {

    /**
     * 配置JSON utils
     *
     * @return
     */
    @Bean
    public UtilsCustomizer jsonUtilCustomizer() {
        return context ->
                Optional.ofNullable(ObjectMapper.class)
                        .map(context::getBeanProvider)
                        .map(ObjectProvider::getIfAvailable)
                        .ifPresent(JSONUtils::setGlobalMapper);
    }

    /**
     * 配置JSON utils
     *
     * @return
     */
    @Bean
    public UtilsCustomizer requestUtilCustomizer() {
        return context ->
                Optional.ofNullable(Validator.class)
                        .map(context::getBeanProvider)
                        .map(ObjectProvider::getIfAvailable)
                        .ifPresent(RequestUtils::setGlobalValidator);
    }
}
