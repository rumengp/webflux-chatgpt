package com.anii.querydsl.gpt;

import java.util.List;

public record Response(
        String id,
        String object,
        Long created,
        List<ChatChoice> choices,
        Usage usage
) {
}
