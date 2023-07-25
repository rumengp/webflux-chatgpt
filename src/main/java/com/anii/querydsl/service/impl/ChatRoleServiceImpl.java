package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstant;
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
        // 角色名用户下唯一
        return assertNotExist(request.nickName())
                .then(Mono.just(chatRole))
                .doOnNext(role -> role.setType(ChatRoleTypeEnum.USER))
                .flatMap(repository::save);
    }

    @Override
    public Mono<ChatRole> updateById(Long id, ChatRoleCreateRequest request) {
        ChatRole chatRole = MAPPER.toDo(request);
        chatRole.setId(id);
        return assertExist(id)
                .then(assertNotExist(request.nickName()))
                .then(Mono.just(chatRole))
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
        return Mono.just(id)
                .zipWith(UserContextHolder.getUsername(), repository::deleteByIdAndUsername)
                .then();
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
                .zipWith(Mono.just(nickName), repository::existsByUsernameAndAndNickName)
                .flatMap(Function.identity())
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(() -> new BusinessException(BusinessConstant.RESOURCE_NAME_EXISTS, BusinessConstant.RESOURCE_NAME_EXISTS_CODE)))
                .then();
    }

}
