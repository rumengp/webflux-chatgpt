package com.anii.querydsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WebfluxR2dbcQuerydslApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxR2dbcQuerydslApplication.class, args);
    }

}
