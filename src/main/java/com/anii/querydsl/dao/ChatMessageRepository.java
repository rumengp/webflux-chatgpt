package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatMessage;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;

public interface ChatMessageRepository extends QuerydslR2dbcRepository<ChatMessage, Long> {
}
