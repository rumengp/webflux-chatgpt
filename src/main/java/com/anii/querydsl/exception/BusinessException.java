package com.anii.querydsl.exception;

import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final String code;

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
