package com.anii.querydsl.request.chat;

import com.anii.querydsl.enums.chat.ModelTypeEnum;
import jakarta.validation.constraints.NotNull;

public record ChatUpdateRequest(
        @NotNull(message = "id不能为空") Long id,
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
