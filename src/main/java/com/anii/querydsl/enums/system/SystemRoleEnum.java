package com.anii.querydsl.enums.system;

import com.anii.querydsl.enums.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SystemRoleEnum implements IBaseEnum {

    ADMIN("ADMIN", "系统管理员"),
    USER("USER", "普通用户");

    private final String code;

    private final String description;

}
