package com.anii.querydsl.service;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.role.ChatRoleCreateRequest;
import reactor.core.publisher.Mono;

public interface IChatRoleService extends IService<ChatRole, Long> {

    Mono<ChatRole> saveChatRole(ChatRoleCreateRequest chatRoleCreateRequest);

    Mono<ChatRole> updateById(Long id, ChatRoleCreateRequest request);

}
