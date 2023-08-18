package com.anii.querydsl.vo;

import com.anii.querydsl.enums.chat.MessageTypeEnum;

public record ChatMessageVo(
        Long id,
        MessageTypeEnum type,
        String content
) {
}
