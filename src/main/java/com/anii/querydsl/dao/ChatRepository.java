package com.anii.querydsl.dao;

import com.anii.querydsl.entity.Chat;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import reactor.core.publisher.Mono;

public interface ChatRepository extends QuerydslR2dbcRepository<Chat, Long> {

    Mono<Void> deleteByIdAndUsername(Long id, String username);
}
