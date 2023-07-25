package com.anii.querydsl.service;

import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IChatService extends IService<Chat, Long> {
    Mono<List<Chat>> findAllByRole(ChatRoleQueryRequest chatRoleQueryRequest);
}
