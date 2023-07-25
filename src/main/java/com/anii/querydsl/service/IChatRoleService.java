package com.anii.querydsl.service;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.role.ChatRoleCreateRequest;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

public interface IChatRoleService extends IService<ChatRole, Long> {

    Mono<ChatRole> saveChatRole(ChatRoleCreateRequest chatRoleCreateRequest);

    Mono<ChatRole> updateById(Long id, ChatRoleCreateRequest request);

    @Query("SELECT COUNT(*) FROM chat_role WHERE id = :id AND (username=:username OR type='SYSTEM')")
    Mono<Boolean> existsByIdAndUsername(Long id, String username);
}
