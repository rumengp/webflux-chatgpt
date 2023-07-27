package com.anii.querydsl.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(
        String role,
        String content,
        String name,
        @JsonProperty("function_all")
        String functionAll) {
}
