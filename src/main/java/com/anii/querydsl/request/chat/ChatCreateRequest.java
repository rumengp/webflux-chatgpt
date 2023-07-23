package com.anii.querydsl.request.chat;

import jakarta.validation.constraints.NotNull;

public record ChatCreateRequest(
        @NotNull Long roleId
) {
}

