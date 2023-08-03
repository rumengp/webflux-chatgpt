package com.anii.querydsl.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessConstantEnum {

    USERNAME_EXIST("10100", "该用户已存在"),
    AUTH_ERROR("10101", "用户名或密码错误"),
    RESOURCE_NAME_EXISTS("10411", "该名称已存在"),
    RESOURCE_NOT_FOUND("10404", "该资源不存在"),
    MINIO_PUT_OBJECT_ERROR("20500", "文件上传失败"),
    MINIO_GET_OBJECT_ERROR("20501", "文件下载失败"),
    JSON_PARSE_FAILED("21500", "JSON解析失败"),
    ;

    private final String code;

    private final String description;
}
