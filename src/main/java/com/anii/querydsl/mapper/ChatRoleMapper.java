package com.anii.querydsl.mapper;

import com.anii.querydsl.entity.ChatRole;
import com.anii.querydsl.request.chat.ChatRoleCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChatRoleMapper {
    ChatRoleMapper MAPPER = Mappers.getMapper(ChatRoleMapper.class);

    ChatRole toDo(ChatRoleCreateRequest request);
}
