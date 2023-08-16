package com.anii.querydsl.common;

import org.springframework.context.ApplicationContext;

/**
 * 用来将容器中的对象注入到静态工具类中
 */
@FunctionalInterface
public interface UtilsCustomizer {

    void customize(ApplicationContext context);
}
