package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatImage;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;

public interface ChatImageRepository extends QuerydslR2dbcRepository<ChatImage, Long> {

    @Query("SELECT * FROM chat_image WHERE username=:username AND id > :preId ORDER BY create_time ASC LIMIT :num")
    Flux<ChatImage> findAllRollout(String username, Long preId, Integer num);
}
