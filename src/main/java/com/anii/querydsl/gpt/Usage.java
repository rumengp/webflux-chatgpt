package com.anii.querydsl.gpt;

public record Usage(
        Long promptTokens,
        Long completionTokens,
        Long totalTokens
) {
}
