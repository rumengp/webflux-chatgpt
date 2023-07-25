package com.anii.querydsl.dao;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.enums.chat.ChatRoleTypeEnum;
import com.infobip.spring.data.r2dbc.QuerydslR2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRoleRepository extends QuerydslR2dbcRepository<ChatRole, Long> {

    Mono<Boolean> existsByUsernameAndNickName(String username, String nickName);

    // @Query("SELECT COUNT(*) FROM chat_role cr WHERE username = :username AND nick_name = :nickName AND id != :id")
    Mono<Boolean> existsByUsernameAndNickNameAndIdNot(String username, String nickName, Long id);

    Mono<ChatRole> findByIdAndUsername(Long id, String username);

    Mono<Void> deleteByIdAndUsername(Long id, String username);

    Mono<Boolean> existsByIdAndUsername(Long id, String username);

    Flux<ChatRole> findAllByUsernameOrType(String username, ChatRoleTypeEnum type);
}
