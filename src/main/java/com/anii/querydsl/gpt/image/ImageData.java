package com.anii.querydsl.gpt.image;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageData(@JsonProperty("b64_json") String b64Json,
                        String url) {
}