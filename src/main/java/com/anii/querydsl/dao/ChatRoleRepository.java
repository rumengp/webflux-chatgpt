package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.enums.chat.ChatRoleTypeEnum;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRoleRepository extends QuerydslR2dbcRepository<ChatRole, Long> {

    Mono<Boolean> existsByUsernameAndAndNickName(String username, String nickName);

    Mono<ChatRole> findByIdAndUsername(Long id, String username);

    Mono<Void> deleteByIdAndUsername(Long id, String username);

    Mono<Boolean> existsByIdAndUsername(Long id, String username);

    Flux<ChatRole> findAllByUsernameOrType(String username, ChatRoleTypeEnum type);
}
