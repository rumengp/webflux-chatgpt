package com.anii.querydsl.common.utils;

import com.anii.querydsl.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class UserContextHolder {

    public static Mono<String> getUsername() {
        return getToken()
                .map(UsernamePasswordAuthenticationToken::getName);
    }

    public static Mono<Long> getUserId() {
        return getToken()
                .map(UsernamePasswordAuthenticationToken::getDetails)
                .cast(User.class)
                .map(User::getId);
    }

    private static Mono<UsernamePasswordAuthenticationToken> getToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(UsernamePasswordAuthenticationToken.class);
    }

}
