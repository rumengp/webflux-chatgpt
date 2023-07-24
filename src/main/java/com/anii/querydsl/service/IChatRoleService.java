package com.anii.querydsl.service;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.ChatRoleCreateRequest;
import reactor.core.publisher.Mono;

public interface IChatRoleService extends IService<ChatRole, Long> {
    Mono<ChatRole> saveChatRole(ChatRoleCreateRequest chatRoleCreateRequest);

    Mono<ChatRole> findByIdAndUsername(Long id);
}
