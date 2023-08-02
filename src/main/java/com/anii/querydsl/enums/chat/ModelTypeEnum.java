package com.anii.querydsl.enums.chat;

import com.anii.querydsl.enums.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModelTypeEnum implements IBaseEnum {

    GPT_35_TURBO("gpt-3.5-turbo", "GPT_35_TURBO"),
    GPT_35_TURBO_0613("gpt-3.5-turbo-0613", "GPT_35_TURBO_0613"),
    GPT_35_TURBO_16K("gpt-3.5-turbo-16k", "GPT_35_TURBO_16K"),
    GPT_35_TURBO_16K_0613("gpt-gpt-3.5-turbo-16k-0613.5-turbo-16k", "gpt-3.5-turbo-16k-0613"),
    GPT_4("gpt-4", "GPT_4"),
    GPT_4_0613("gpt-4-0613", "GPT_4_0613"),
    GPT_4_32K("gpt-4-32k", "GPT_4_32K"),
    GPT_4_32K_0613("gpt-4-32k-0613", "GPT_4_32K");

    private final String code;

    private final String description;
}
