package com.anii.querydsl.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record Completion(
        String model,
        List<Message> messages,
        List<Function> functions,
        @JsonProperty("function_all")
        String functionAll,
        Float temperature,
        @JsonProperty("top_p")
        Float topP,
        Integer n,
        Boolean stream,
        List<String> stop,
        @JsonProperty("max_tokens")
        Integer maxTokens,
        @JsonProperty("presence_penalty")
        Float presencePenalty,
        @JsonProperty("frequency_penalty")
        Float frequencyPenalty,
        @JsonProperty("logit_bias")
        Map<String, String> logitBias,
        String user
) {
}
