package com.anii.querydsl.enums.system;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SystemRoleEnum {

    ADMIN("ADMIN", "系统管理员"),
    USER("USER", "普通用户");

    private final String code;

    private final String description;

}
