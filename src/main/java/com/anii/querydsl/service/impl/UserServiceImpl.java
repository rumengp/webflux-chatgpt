package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstant;
import com.anii.querydsl.dao.UserRepository;
import com.anii.querydsl.entity.User;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.request.UserRegisterReq;
import com.anii.querydsl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public Mono<User> register(UserRegisterReq userReq) {

        return Mono.just(userReq)
                .doOnNext(r -> {
                    userRepository.existsByUsername(userReq.username())
                            .filter(Boolean.FALSE::equals)
                            .switchIfEmpty(Mono.error(new BusinessException(BusinessConstant.USERNAME_EXIST, BusinessConstant.USERNAME_EXIST_CODE)));
                })
                .map(req -> {
                    return User.builder().username(req.username())
                            .password(encoder.encode(req.password()))
                            .build();
                })
                .flatMap(userRepository::save);
    }
}
