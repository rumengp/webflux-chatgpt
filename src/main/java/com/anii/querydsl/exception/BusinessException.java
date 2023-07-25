package com.anii.querydsl.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final String code;

    public BusinessException(String message, String code) {
        this(message, code, null);
    }

    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
