package com.anii.querydsl.gpt.exception;

public record GPTErrorBody(ErrorBody error) {
}

record ErrorBody(String message, String type, String param, String code) {
}
