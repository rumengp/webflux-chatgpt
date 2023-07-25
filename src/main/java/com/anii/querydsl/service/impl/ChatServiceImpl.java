package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatRepository;
import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.service.IChatService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatServiceImpl extends ServiceImpl<ChatRepository, Chat, Long> implements IChatService {

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::deleteByIdAndUsername)
                .then();
    }
}
