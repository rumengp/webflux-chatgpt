package com.anii.querydsl.service;

import com.anii.querydsl.entity.User;
import com.anii.querydsl.request.UserRegisterReq;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserService extends IService<User, Long> {
    Mono<User> register(UserRegisterReq userReq);

    Mono<List<User>> findAllPage();
}
