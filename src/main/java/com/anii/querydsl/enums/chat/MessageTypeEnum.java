package com.anii.querydsl.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageTypeEnum {

    SYSTEM("SYSTEM", "系统"),
    USER("USER", "用户"),
    ASSISTANT("ASSISTANT", "助手");

    private final String code;

    private final String description;
}
