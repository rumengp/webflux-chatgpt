package com.anii.querydsl.gpt.exception;

public final class InvalidApiKeyException extends GPTException {
    public InvalidApiKeyException() {
        super("指定的api key无效", "invalid_api_key");
    }
}
