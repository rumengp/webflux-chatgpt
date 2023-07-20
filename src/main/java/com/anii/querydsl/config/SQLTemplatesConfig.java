package com.anii.querydsl.config;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SQLTemplatesConfig {

    @Bean
    public SQLTemplates sqlTemplates() {
        return new MySQLTemplates();
    }
}
