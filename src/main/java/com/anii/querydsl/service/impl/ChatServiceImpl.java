package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatMessageRepository;
import com.anii.querydsl.dao.ChatRepository;
import com.anii.querydsl.dao.ChatRoleRepository;
import com.anii.querydsl.entity.Chat;
import com.anii.querydsl.entity.ChatMessage;
import com.anii.querydsl.enums.chat.MessageTypeEnum;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.exception.NotFoundException;
import com.anii.querydsl.gpt.GPTClient;
import com.anii.querydsl.gpt.chat.Message;
import com.anii.querydsl.gpt.exception.GPTException;
import com.anii.querydsl.mapper.MessageMapper;
import com.anii.querydsl.request.chat.ChatCreateRequest;
import com.anii.querydsl.request.chat.ChatMessageRequest;
import com.anii.querydsl.request.chat.ChatUpdateRequest;
import com.anii.querydsl.request.chat.role.ChatRoleQueryRequest;
import com.anii.querydsl.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.anii.querydsl.mapper.ChatMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl extends ServiceImpl<ChatRepository, Chat, Long> implements IChatService {

    private final ChatRoleRepository roleRepository;

    private final ChatMessageRepository messageRepository;

    private final GPTClient client;

    @Override
    @Transactional
    public Mono<Void> deleteById(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::deleteByIdAndUsername)
                .then(messageRepository.deleteByChatId(id))
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

        Flux<Message> commandMessage = chat.map(Chat::getCommand).map(Message::ofSystemContent).flux();
        Flux<Message> chatMessages = chat.flatMapMany(c -> messageRepository.findAllByChatIdLimit(c.getId(), c.getContextNum()))
                .map(MessageMapper.MAPPER::toMessage);
        Flux<Message> newMessage = Mono.just(req.content()).map(Message::ofUserContent).flux();
        Flux<String> replays = Flux.concat(commandMessage, chatMessages, newMessage)
                .collectList()
                .zipWith(chat, (messages, c) ->
                        MessageMapper.MAPPER.toCompletion(c, messages)
                )
                .flatMapMany(client::chatStream)
                .onErrorMap(e -> e instanceof GPTException, e -> new BusinessException(e.getMessage(), ((GPTException) e).getCode()))
                .cache(); // 必须使用cache转化为热源，否则在保存和返回前端时发起多次请求

        Mono<String> saveUserMessage = Mono.just(req.content())
                .map(content ->
                        ChatMessage.builder()
                                .chatId(id)
                                .type(MessageTypeEnum.USER)
                                .content(content)
                                .build())
                .flatMap(messageRepository::save)
                .then(Mono.empty());
        Mono<String> saveReplayMessage = replays.collect(Collectors.joining(""))
                .map(c ->
                        ChatMessage.builder()
                                .chatId(id)
                                .content(c)
                                .type(MessageTypeEnum.ASSISTANT)
                                .build())
                .flatMap(messageRepository::save)
                .then(Mono.empty()); // 采用一个空返回，并连接到返回值，可以保证被spring订阅，从而使用spring security写入的上下文

        return Flux.concat(replays, saveUserMessage, saveReplayMessage);
    }

}
