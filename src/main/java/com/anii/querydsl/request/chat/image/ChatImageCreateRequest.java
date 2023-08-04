package com.anii.querydsl.request.chat.image;

public record ChatImageCreateRequest(
        String prompt,
        Integer n,
        String size,
        String responseFormat
) {
}
