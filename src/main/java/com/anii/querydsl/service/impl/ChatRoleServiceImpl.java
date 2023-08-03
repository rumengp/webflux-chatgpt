package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstantEnum;
import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatRoleRepository;
import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.enums.chat.ChatRoleTypeEnum;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.exception.NotFoundException;
import com.anii.querydsl.request.chat.role.ChatRoleCreateRequest;
import com.anii.querydsl.service.IChatRoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

import static com.anii.querydsl.mapper.ChatRoleMapper.MAPPER;

@Service
public class ChatRoleServiceImpl extends ServiceImpl<ChatRoleRepository, ChatRole, Long> implements IChatRoleService {

    @Override
    public Mono<ChatRole> saveChatRole(ChatRoleCreateRequest request) {
        ChatRole chatRole = MAPPER.toDo(request);
        chatRole.setType(ChatRoleTypeEnum.USER);
        // 角色名用户下唯一
        return assertNotExist(request.nickName())
                .then(Mono.just(chatRole))
                .flatMap(repository::save);
    }

    @Override
    public Mono<ChatRole> updateById(Long id, ChatRoleCreateRequest request) {
        return assertNotExist(id, request.nickName())
                .then(this.findById(id))
                .doOnNext(role -> MAPPER.copyProperties(request, role))
                .flatMap(repository::save);
    }

    @Override
    public Mono<List<ChatRole>> findAll() {
        return UserContextHolder.getUsername()
                .zipWith(Mono.just(ChatRoleTypeEnum.SYSTEM), repository::findAllByUsernameOrType)
                .flatMapMany(Function.identity())
                .collectList();
    }

    @Override
    public Mono<ChatRole> findById(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::findByIdAndUsername)
                .flatMap(Function.identity())
                .switchIfEmpty(Mono.error(() -> new NotFoundException()));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return UserContextHolder.getUsername()
                .flatMap(username -> repository.deleteByIdAndUsername(id, username));
    }

    private Mono<Void> assertExist(Long id) {
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::existsByIdAndUsername)
                .flatMap(Function.identity())
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(() -> new NotFoundException()))
                .then();
    }

    private Mono<Void> assertNotExist(String nickName) {
        return UserContextHolder.getUsername()
                .flatMap(username -> repository.existsByUsernameAndNickName(username, nickName))
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(() -> new BusinessException(BusinessConstantEnum.RESOURCE_NAME_EXISTS)))
                .then();
    }

    private Mono<Void> assertNotExist(Long id, String nickName) {
        return UserContextHolder.getUsername()
                .flatMap(username -> repository.existsByUsernameAndNickNameAndIdNot(username, nickName, id))
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(() -> new BusinessException(BusinessConstantEnum.RESOURCE_NAME_EXISTS)))
                .then();
    }

}
