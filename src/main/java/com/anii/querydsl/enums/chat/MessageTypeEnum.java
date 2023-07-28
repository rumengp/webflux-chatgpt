package com.anii.querydsl.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageTypeEnum {

    SYSTEM("system", "系统"),
    USER("user", "用户"),
    ASSISTANT("assistant", "助手");

    private final String code;

    private final String description;
}
