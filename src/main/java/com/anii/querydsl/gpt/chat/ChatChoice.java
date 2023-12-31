package com.anii.querydsl.gpt.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatChoice(
        Long index,
        Message message,
        Message delta,
        @JsonProperty("finish_reason")
        String finishReason) {
}
