package com.anii.querydsl.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessConstantEnum {

    USERNAME_EXIST("10100", "该用户已存在"),
    AUTH_ERROR("10101", "用户名或密码错误"),
    RESOURCE_NAME_EXISTS("10411", "该名称已存在"),
    RESOURCE_NOT_FOUND("10404", "该资源不存在");

    private final String code;

    private final String description;
}