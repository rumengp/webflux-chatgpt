package com.anii.querydsl.service.impl;

import com.anii.querydsl.common.BusinessConstant;
import com.anii.querydsl.common.utils.UserContextHolder;
import com.anii.querydsl.dao.ChatRoleRepository;
import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.exception.BusinessException;
import com.anii.querydsl.request.chat.ChatRoleCreateRequest;
import com.anii.querydsl.service.IChatRoleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.anii.querydsl.mapper.ChatRoleMapper.MAPPER;

@Service
public class ChatRoleServiceImpl extends ServiceImpl<ChatRoleRepository, ChatRole, Long> implements IChatRoleService {

    @Override
    public Mono<ChatRole> saveChatRole(ChatRoleCreateRequest request) {
        ChatRole chatRole = MAPPER.toDo(request);
        // 角色名用户下唯一
        return UserContextHolder.getUsername()
                .flatMap(username -> repository.existsByUsernameAndAndNickName(username, request.nickName()))
                .filter(Boolean.FALSE::equals)
                .switchIfEmpty(Mono.error(() -> new BusinessException(BusinessConstant.RESOURCE_NAME_EXISTS, BusinessConstant.RESOURCE_NAME_EXISTS_CODE)))
                .then(Mono.just(chatRole))
                .zipWith(UserContextHolder.getUsername(),
                        (role, name) -> {
                            role.setUsername(name);
                            return role;
                        }
                )
                .flatMap(repository::save);
    }
}
