package com.anii.querydsl.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelTypeEnum {

    GPT_35_TURBO("GPT_35_TURBO", "GPT_35_TURBO"),
    GPT_35_TURBO_0613("GPT_35_TURBO_0613", "GPT_35_TURBO_0613"),
    GPT_35_TURBO_16K("GPT_35_TURBO_16K", "GPT_35_TURBO_16K"),
    GPT_4("GPT_4", "GPT_4"),
    GPT_4_0613("GPT_4_0613", "GPT_4_0613"),
    GPT_4_32K("GPT_4_32K", "GPT_4_32K");

    private final String code;

    private final String description;
}
