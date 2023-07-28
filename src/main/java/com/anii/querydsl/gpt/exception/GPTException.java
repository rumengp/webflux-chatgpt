package com.anii.querydsl.gpt.exception;

import lombok.Getter;

@Getter
public sealed class GPTException extends RuntimeException
        permits ContextLengthExceededException, InvalidApiKeyException {

    private final String code;

    public GPTException(String message, String code) {
        super(message);
        this.code = code;
    }

    public static GPTException ofResponse(GPTErrorBody error) {
        return switch (error.error().code()) {
            case "context_length_exceeded" -> new ContextLengthExceededException();
            case "invalid_api_key" -> new InvalidApiKeyException();
            default -> new GPTException(error.error().message(), error.error().code());
        };
    }
}
