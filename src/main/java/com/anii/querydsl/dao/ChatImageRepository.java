package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatImage;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;

public interface ChatImageRepository extends QuerydslR2dbcRepository<ChatImage, Long> {
}
