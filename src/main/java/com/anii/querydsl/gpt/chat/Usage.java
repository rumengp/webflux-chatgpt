package com.anii.querydsl.gpt.chat;

public record Usage(
        Long promptTokens,
        Long completionTokens,
        Long totalTokens
) {
}
