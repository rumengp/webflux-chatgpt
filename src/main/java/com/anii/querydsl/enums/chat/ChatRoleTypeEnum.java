package com.anii.querydsl.enums.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatRoleTypeEnum {

    USER("USER", "用户角色"),
    SYSTEM("SYSTEM", "系统内置角色");

    private final String code;

    private final String description;
}
