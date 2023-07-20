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

import static com.anii.querydsl.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public Mono<User> register(UserRegisterReq userReq) {
        return userRepository.existsByUsername(userReq.username())
                .flatMap(exist -> exist ? Mono.error(new BusinessException(BusinessConstant.USERNAME_EXIST, BusinessConstant.USERNAME_EXIST_CODE))
                        : Mono.empty())
                .then(Mono.just(userReq))
                .map(USER_MAPPER::toDo)
                .doOnNext(user -> user.setPassword(encoder.encode(user.getPassword())))
                .flatMap(userRepository::save);
    }
}
