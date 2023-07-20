package com.anii.querydsl.dao;

import com.anii.querydsl.entity.User;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends QuerydslR2dbcRepository<User, Long> {

    Mono<User> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);
}
