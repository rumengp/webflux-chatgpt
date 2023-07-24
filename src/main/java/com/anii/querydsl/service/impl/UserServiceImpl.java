package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstant;
import com.anii.querydsl.config.jwt.JwtTokenProvider;
import com.anii.querydsl.dao.UserRepository;
import com.anii.querydsl.entity.QUser;
import com.anii.querydsl.entity.User;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.request.AuthRequest;
import com.anii.querydsl.request.UserRegisterReq;
import com.anii.querydsl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.anii.querydsl.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserRepository, User, Long> implements UserService {

    private final PasswordEncoder encoder;

    private final JwtTokenProvider tokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    private final R2dbcEntityTemplate entityTemplate;

    @Override
    public Mono<User> register(UserRegisterReq userReq) {
        return repository.existsByUsername(userReq.username())
                .flatMap(exist -> exist ? Mono.error(new BusinessException(BusinessConstant.USERNAME_EXIST, BusinessConstant.USERNAME_EXIST_CODE))
                        : Mono.empty())
                .then(Mono.just(userReq))
                .map(USER_MAPPER::toDo)
                .doOnNext(user -> user.setPassword(encoder.encode(user.getPassword())))
                .flatMap(repository::save);
    }

    @Override
    public Mono<List<User>> findAllPage() {
        return entityTemplate.select(User.class)
                .matching(Query.empty())
                .all().collectList();
    }

    @Override
    public Mono<String> login(AuthRequest authRequest) {
        return this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()))
                .onErrorMap(ex -> new BusinessException(BusinessConstant.AUTH_ERROR, BusinessConstant.AUTH_ERROR_CODE, ex))
                .zipWith(repository.findByUsername(authRequest.username()), (auth, user) -> {
                    if (auth instanceof AbstractAuthenticationToken t) {
                        t.setDetails(user);
                    }
                    return auth;
                })
                .map(tokenProvider::createToken);
    }

    public Mono<List<User>> page() {
        QUser user = QUser.user;
        QUser m = new QUser("m");
        return repository.query(query ->
                        query.select(repository.entityProjection())
                                .from(user)
                                .leftJoin(m).on(user.id.eq(m.id)))
                .all().collectList();
    }

}
