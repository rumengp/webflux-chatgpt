package com.anii.querydsl.controller.chat;

import com.anii.querydsl.service.IChatMessageService;
import org.springframework.stereotype.Component;

@Component
public record ChatMessageHandler(IChatMessageService chatMessageService) {
}
