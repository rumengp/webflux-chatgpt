package com.anii.querydsl.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoleTypeEnum {

    USER("user", "用户角色"),
    SYSTEM("system", "系统内置角色"),
    ASSISTANT("assistant", "助手"),
    FUNCTION("function", "函数");

    private final String code;

    private final String description;
}
