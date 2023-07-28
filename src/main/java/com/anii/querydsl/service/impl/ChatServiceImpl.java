package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatMessageRepository;
import com.anii.querydsl.dao.ChatRepository;
import com.anii.querydsl.dao.ChatRoleRepository;
import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.enums.chat.MessageTypeEnum;
import com.anii.querydsl.exception.NotFoundException;
import com.anii.querydsl.gpt.Completion;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.Message;
import com.anii.querydsl.mapper.MessageMapper;
import com.anii.querydsl.request.chat.ChatCreateRequest;
import com.anii.querydsl.request.chat.ChatMessageRequest;
import com.anii.querydsl.request.chat.ChatUpdateRequest;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import com.anii.querydsl.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.anii.querydsl.mapper.ChatMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl extends ServiceImpl<ChatRepository, Chat, Long> implements IChatService {

    private final ChatRoleRepository roleRepository;

    private final ChatMessageRepository messageRepository;

    private final GPTClient client;

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

    @Override
    public Mono<Chat> createNewChat(ChatCreateRequest request) {
        // 先检查是否存在该角色
        return roleRepository.findById(request.roleId())
                .switchIfEmpty(Mono.error(() -> new NotFoundException()))
                .map(chatRole -> MAPPER.fromChatRole(chatRole))
                .flatMap(repository::save);
    }

    @Override
    public Mono<Chat> updateById(Long id, ChatUpdateRequest req) {
        return UserContextHolder.getUsername()
                .flatMap(username -> repository.findByIdAndUsername(id, username))
                .switchIfEmpty(Mono.error(() -> new NotFoundException()))
                .doOnNext(chat -> MAPPER.copyProperties(req, chat))
                .flatMap(repository::save);
    }

    @Override
    public Flux<String> chatStream(Long id, ChatMessageRequest req) {
        // 查找chat 配置
        Mono<Chat> chat = UserContextHolder.getUsername()
                .flatMap(username -> repository.findByIdAndUsername(id, username))
                .cache();

        Flux<Message> commandMessage = chat.flux().map(c ->
                Message.builder().role(MessageTypeEnum.SYSTEM.getCode())
                        .content(c.getCommand())
                        .build()
        );

        Flux<Message> chatMessages = chat.flatMapMany(c -> messageRepository.findAllByChatIdLimit(c.getId(), c.getContextNum()))
                .map(MessageMapper.MAPPER::toMessage);

        Flux.concat(commandMessage, chatMessages)
                .collectList()
                .zipWith(chat, (messages, c) -> {

                })

        return null;


    }

}
