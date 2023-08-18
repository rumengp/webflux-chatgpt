package com.anii.querydsl.service;

import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.request.chat.ChatCreateRequest;
import com.anii.querydsl.request.chat.ChatMessageRequest;
import com.anii.querydsl.request.chat.ChatUpdateRequest;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IChatService extends IService<Chat, Long> {

    Flux<Chat> findAllByRole(ChatRoleQueryRequest chatRoleQueryRequest);

    Mono<Chat> createNewChat(ChatCreateRequest chatCreateRequest);

    Mono<Chat> updateById(Long id, ChatUpdateRequest req);

    Flux<String> chatStream(Long id, ChatMessageRequest req);
}
