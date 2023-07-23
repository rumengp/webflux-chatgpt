package com.anii.querydsl.dao;

import com.anii.querydsl.entity.Chat;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;

public interface ChatRepository extends QuerydslR2dbcRepository<Chat, Long> {
}
