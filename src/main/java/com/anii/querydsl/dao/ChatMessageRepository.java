package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatMessage;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends QuerydslR2dbcRepository<ChatMessage, Long> {

    @Query("SELECT * FROM chat_message WHERE chat_id=:chatId ORDER BY create_time LIMIT :limit")
    Flux<ChatMessage> findAllByChatIdLimit(Long chatId, Integer limit);
}
