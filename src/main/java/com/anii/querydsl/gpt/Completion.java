package com.anii.querydsl.gpt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Completion {

    private String model;

    private List<Message> messages;

    private List<Function> functions;

    @JsonProperty("function_all")
    private String functionAll;

    private Double temperature;

    @JsonProperty("top_p")
    private Double topP;

    private Integer n;

    private Boolean stream;

    private List<String> stop;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map<String, String> logitBias;

    private String user;
}
