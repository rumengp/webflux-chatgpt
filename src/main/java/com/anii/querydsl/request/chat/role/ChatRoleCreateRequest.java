package com.anii.querydsl.request.chat.role;

import com.anii.querydsl.enums.chat.ModelTypeEnum;
import jakarta.validation.constraints.NotNull;

public record ChatRoleCreateRequest(
        @NotNull(message = "角色指令不能为空")
        String command,
        @NotNull(message = "角色模型不能为空")
        ModelTypeEnum model,
        @NotNull(message = "角色昵称不能为空")
        String nickName,
        String welcome,
        String mark,
        String picture,
        Integer contextNum,
        Integer maxReplay,
        Float propertyRandom,
        Float propertyWord,
        Float propertyTopic,
        Float propertyRepeat
) {
}
