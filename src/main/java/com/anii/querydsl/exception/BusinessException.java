package com.anii.querydsl.exception;

import com.anii.querydsl.common.BusinessConstantEnum;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private final String code;

    public BusinessException(BusinessConstantEnum constantEnum) {
        this(constantEnum.getDescription(), constantEnum.getCode());
    }

    public BusinessException(BusinessConstantEnum constantEnum, Throwable e) {
        this(constantEnum.getDescription(), constantEnum.getCode(), e);
    }

    public BusinessException(String message, String code) {
        this(message, code, null);
    }

    public BusinessException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
