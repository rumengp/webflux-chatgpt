package com.anii.querydsl.gpt.exception;

public non-sealed class ContextLengthExceededException extends GPTException {
    public ContextLengthExceededException() {
        super("字数超出模型限制", "context_length_exceeded");
    }
}
