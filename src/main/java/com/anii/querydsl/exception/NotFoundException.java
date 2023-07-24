package com.anii.querydsl.exception;

import com.anii.querydsl.common.BusinessConstant;

public class NotFoundException extends BusinessException {

    public NotFoundException() {
        super(BusinessConstant.RESOURCE_NOT_FOUND, BusinessConstant.RESOURCE_NOT_FOUND_CODE);
    }

    public NotFoundException(Throwable cause) {
        super(BusinessConstant.RESOURCE_NOT_FOUND, BusinessConstant.RESOURCE_NOT_FOUND_CODE, cause);
    }
}
