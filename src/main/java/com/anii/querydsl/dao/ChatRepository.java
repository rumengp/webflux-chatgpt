package com.anii.querydsl.dao;

import com.anii.querydsl.entity.Chat;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRepository extends QuerydslR2dbcRepository<Chat, Long> {

    Mono<Void> deleteByIdAndUsername(Long id, String username);

    Mono<Chat> findByIdAndUsername(Long id, String username);

    Flux<Chat> findAllByUsername(String username);

    Flux<Chat> findAllByUsernameAndRoleId(String username, Long roleId);

    Mono<Boolean> existsByIdAndUsername(Long id, String username);
}
