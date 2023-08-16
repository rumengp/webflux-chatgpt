package com.anii.querydsl.exception;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.stream.Collectors;

public class ValidException extends RuntimeException {

    private final transient Errors errors;

    public ValidException(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String getMessage() {
        return errors.getFieldErrors().stream()
                .map(this::parseFieldError)
                .collect(Collectors.joining(","));
    }

    private String parseFieldError(FieldError fieldError) {
        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String defaultMessage = fieldError.getDefaultMessage();
        return String.format("[%s=%s]: message=[%s]", field, rejectedValue, defaultMessage);
    }
}
