package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatRepository;
import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.exception.NotFoundException;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import com.anii.querydsl.service.IChatService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatRepository, Chat, Long> implements IChatService {

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::deleteByIdAndUsername)
                .then();
    }

    @Override
    public Mono<Chat> findById(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::findByIdAndUsername)
                .flatMap(Function.identity())
                .switchIfEmpty(Mono.error(() -> new NotFoundException()));
    }

    @Override
    public Mono<List<Chat>> findAllByRole(ChatRoleQueryRequest request) {
        return UserContextHolder.getUsername()
                .flatMapMany(username -> {
                    if (Objects.isNull(request.roleId())) {
                        return repository.findAllByUsername(username);
                    }
                    return repository.findAllByUsernameAndRoleId(username, request.roleId());
                }).collectList();
    }
}
