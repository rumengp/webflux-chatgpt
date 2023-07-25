package com.anii.querydsl.request.chat;

import com.anii.querydsl.enums.chat.ModelTypeEnum;

public record ChatUpdateRequest(
        String title,
        ModelTypeEnum model,
        Integer contextNum,
        Integer maxReplay,
        Float propertyRandom,
        Float propertyWord,
        Float propertyTopic,
        Float propertyRepeat
) {
}
