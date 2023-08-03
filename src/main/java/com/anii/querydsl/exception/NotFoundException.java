package com.anii.querydsl.exception;

import com.anii.querydsl.common.BusinessConstantEnum;

public class NotFoundException extends BusinessException {

    public NotFoundException() {
        super(BusinessConstantEnum.RESOURCE_NOT_FOUND);
    }

    public NotFoundException(Throwable cause) {
        super(BusinessConstantEnum.RESOURCE_NOT_FOUND, cause);
    }
}
