package com.anii.querydsl.service;

import com.anii.querydsl.entity.User;
import com.anii.querydsl.request.UserRegisterReq;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(UserRegisterReq userReq);
}
